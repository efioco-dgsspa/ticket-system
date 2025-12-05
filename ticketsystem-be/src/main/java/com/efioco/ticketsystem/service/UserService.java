package com.efioco.ticketsystem.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.RoleDTO;
import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.entity.RoleEntity;
import com.efioco.ticketsystem.entity.UserEntity;
import com.efioco.ticketsystem.exceptions.ResourceNotFoundException;
import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.mapper.UserMapper;
import com.efioco.ticketsystem.repository.RoleRepository;
import com.efioco.ticketsystem.repository.UserRepository;
import com.efioco.ticketsystem.request.AuthRequest;
import com.efioco.ticketsystem.request.UserRequest;
import com.efioco.ticketsystem.response.UserResponse;
import com.efioco.ticketsystem.specification.UserSpecification;

import io.micrometer.common.util.StringUtils;

@Service
public class UserService implements UserServiceInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private MailService mailService;
	
	@Autowired
    private UserMapper userMapper;
	
	@Override
	public void createUserIfNotExists(String username, String email, String password, 
			Set<RoleEntity> roles) {
		if (!userRepository.existsByEmail(email)) {
            UserEntity u = new UserEntity();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode(password));
            u.setRoles(roles);
            u.setActive(true);
            userRepository.save(u);
        }
		else {
			throw new UserServiceException("Non e' stato possibile creare l'utente, indirizzo email gia' in uso");
		}
	}
	
	@Override
    public UserResponse createUser(UserRequest userRequest) {
		logger.info("### Inizio processo di creazione utente ###");
		UserDTO userDTO = userRequest.getUser();
		
	    Assert.notNull(userDTO, "UserDTO non può essere null");
	    Assert.hasText(userDTO.getPassword(), "Password obbligatoria");
	    Assert.notEmpty(userDTO.getRoles(), "Almeno un ruolo è obbligatorio");
	    
		UserResponse response = new UserResponse();
    	
	    Set<RoleEntity> rolesToAdd = new HashSet<>();
	    if (userDTO.getRoles() != null) {
	        for (RoleDTO roleDTO : userDTO.getRoles()) {
	            RoleEntity roleEntity = roleRepository.findById(UUID.fromString(roleDTO.getId()))
	                    .orElseThrow(() -> new ResourceNotFoundException("Ruolo non trovato."));
	            rolesToAdd.add(roleEntity);
	        }
	    }

	    UserEntity userEntity = userMapper.toEntity(userDTO);
	    userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	    userEntity.setRoles(rolesToAdd);

	    UserEntity savedUser = userRepository.save(userEntity);
	    
	    response.setUser(userMapper.toDTO(savedUser));
	    
	    logger.info("### Creazione utente completata con successo ###");
	    return response;
    }
	
	@Override
	public UserResponse updateUser(UserRequest userRequest) {
		logger.info("### Inizio processo di modifica dei parametri dell'utente ###");
		UserDTO inputDTO = userRequest.getUser();
        UserResponse response = new UserResponse();

        // 1️⃣ Recupera l'utente dal DB
        UserEntity existingUser = userRepository.findById(UUID.fromString(inputDTO.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato."));

        boolean modified = false;

        Boolean toggleActive = existingUser.getActive();

        // 2️⃣ Aggiorna username (solo se diverso)
        if(StringUtils.isNotBlank(inputDTO.getUsername()) 
        		&& !inputDTO.getUsername().equals(existingUser.getUsername())) {
            existingUser.setUsername(inputDTO.getUsername());
            modified = true;
        }

        // 3️⃣ Aggiorna email (solo se diversa)
        if(StringUtils.isNotBlank(inputDTO.getEmail()) 
        		&& !inputDTO.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(inputDTO.getEmail());
            modified = true;
        }

        // 4️⃣ Aggiorna password 
        if (StringUtils.isNotBlank(inputDTO.getPasswordCorrente()) && 
                StringUtils.isNotBlank(inputDTO.getNuovaPassword())) {
                
        	// Verifica che la passwordCorrente corrisponda a quella dell'utente recuperato a db
        	if (!passwordEncoder.matches(inputDTO.getPasswordCorrente(), existingUser.getPassword())) {
        		logger.warn("Tentativo di cambio password fallito per utente: {}", existingUser.getUsername());
        		throw new UserServiceException("La password corrente non corrisponde a quella salvata a sistema.");
        	}

        	// Se c'è corrispondenza, aggiorna con la nuova password
        	existingUser.setPassword(passwordEncoder.encode(inputDTO.getNuovaPassword()));
        	modified = true;
        }

        // 5️⃣ Aggiorna ruoli (solo se effettivamente diversi)
        if(inputDTO.getRoles() != null && !inputDTO.getRoles().isEmpty()) {
        	
            Set<RoleEntity> newRoles = new HashSet<>();
            for(RoleDTO roleDTO : inputDTO.getRoles()) {
                RoleEntity role = roleRepository.findByName(roleDTO.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Ruolo non trovato: " + roleDTO.getName()));
                newRoles.add(role);
            }
            
            if(!existingUser.getRoles().equals(newRoles)) {
                existingUser.setRoles(newRoles);
                modified = true;
            }
        }

        // 6️⃣ Aggiorna stato attivo (solo se cambia)
        if(existingUser.getActive() != inputDTO.getActive()) {
            existingUser.setActive(inputDTO.getActive());
            modified = true;
        }

        // 7️⃣ Salva modifiche solo se necessario
        if(modified) {
            userRepository.save(existingUser);
            
            String text = "Ciao " + existingUser.getUsername() + ", il tuo account è stato aggiornato con successo.";

            if (existingUser.getActive() != toggleActive) {
                text += " Segnaliamo inoltre che l'account è stato " + (existingUser.getActive() ? "riattivato" : "disattivato") + ".";
            }

            // 8️⃣ Invia mail di notifica
            mailService.sendSimple(existingUser.getEmail(), "Aggiornamento del profilo", text);
        }

        response.setUser(userMapper.toDTO(existingUser));
        
        logger.info("### Modifiche dell'utente completate con successo ###");
        return response;
    }
	
	@Override
	public void deleteUser(String id) throws UserServiceException {
		logger.info("### Inizio processo di eliminazione dell'utente dal sistema ###");
        UserEntity existingUser = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato."));
        
        if (existingUser.getActive()) {
        	throw new UserServiceException("Impossibile eliminare un utente attivo");
        }
        
        userRepository.delete(existingUser);
        
        String subject = "Eliminazione utenza";
        String text = "Ciao " + existingUser.getUsername() + ", la tua utenza è stata cancellata.";
        mailService.sendSimple(existingUser.getEmail(), subject, text);
        logger.info("### Eliminazione dell'utente dal sistema completata con successo ###");
	}
	
	@Override
    public UserResponse getAllUsers() {
		logger.info("### Inizio processo di recupero di tutti gli utenti presenti nel sistema ###");
		UserResponse response = new UserResponse();
		List<UserDTO> users = userMapper.toDTOList(userRepository.findAll());

		response.setUsers(users);
		
		logger.info("### Recupero di tutti gli utenti presenti nel sistema completato con successo ###");
		return response;
	}
	
	@Override
	public UserResponse updateUserActiveStatus(String id, boolean active) {
	    logger.info("### Inizio aggiornamento stato attivo utente con id: {} ###", id);

	    UserEntity existingUser = userRepository.findById(UUID.fromString(id))
	        .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));

	    if (existingUser.getActive() == active) {
	        throw new UserServiceException("L'utente è già nello stato richiesto");
	    }

	    existingUser.setActive(active);
	    userRepository.save(existingUser);

	    // invio email informativa
	    String subject = "Aggiornamento stato account";
	    String text = String.format(
	        "Ciao %s, il tuo account è stato %s.",
	        existingUser.getUsername(),
	        active ? "attivato" : "disattivato"
	    );
	    mailService.sendSimple(existingUser.getEmail(), subject, text);

	    UserResponse response = new UserResponse();
	    response.setUser(userMapper.toDTO(existingUser));

	    logger.info("### Stato attivo aggiornato con successo per utente {} ###", id);
	    return response;
	}
	
	@Override
    public UserResponse searchUsers(UserRequest request) {
		UserResponse response = new UserResponse();
        try {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

            Page<UserEntity> page = userRepository.findAll(new UserSpecification(request), pageable);
            List<UserEntity> entities = page.getContent();

            if (CollectionUtils.isEmpty(entities)) {
                response.setTotalPage(0);
                response.setTotalRows(0l);
                response.setUsers(null);

                return response;
            }

            response.setTotalPage(page.getTotalPages());
            response.setTotalRows(page.getTotalElements());

            response.setUsers(userMapper.toDTOList(entities));
        } catch (Exception e) {
            logger.error("Errore durante il recupero degli utenti con la request: {}", request, e);
            throw new UserServiceException("Errore durante il recupero degli utenti.");
        }
        return response;
    }

    @Override
    public UserResponse getUsersByRole(String role) throws UserServiceException {

        logger.info("### Inizio processo di recupero di utenti a partire dal ruolo: {} ###" , role);
        try {
            UserResponse response = new UserResponse();
            List<UserEntity> users = userRepository.findByRoles_Name(role);
            response.setUsers(userMapper.toDTOList(users));

            logger.info("### Utenti recuperati con successo ###");
            return response;
        } catch (Exception e) {
            logger.error("Errore durante il recupero degli utenti per il ruolo: {}", role, e);
            throw new UserServiceException("Errore durante il recupero degli utenti.");
        }
    }

    @Override
	public UserResponse getUserByUsername(String username) {
		logger.info("### Inizio processo di recupero di un utente a partire dal suo username ###");
	    UserEntity user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con username: " + username));

	    UserResponse response = new UserResponse();
	    response.setUser(userMapper.toDTO(user));
	    
	    logger.info("### Utente recuperato con successo ###");
	    return response;
	}
	
	@Override
	public UserResponse getUserByEmail(String email) {
		logger.info("### Inizio processo di recupero di un utente a partire dalla sua email ###");
	    UserEntity user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con email: " + email));

	    UserResponse response = new UserResponse();
	    response.setUser(userMapper.toDTO(user));
	    
	    logger.info("### Utente recuperato con successo ###");
	    return response;
	}
	
	@Override
	public UserResponse getUserByEmailOrUsername(AuthRequest request) {
		logger.info("### Inizio processo di recupero di un utente per il login ###");
		UserEntity user = userRepository.findByEmailOrUsername(request.getIdentifier())
	            .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con username/email: " + request.getIdentifier()));

	    if (!user.getActive()) {
	        throw new UserServiceException("Utente disattivato. Contattare l’amministratore.");
	    }

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new UserServiceException("Password errata.");
	    }
	    
//		🔹 LOG aggiuntivi per debug
//	    System.out.println("ENCODER CLASS: " + passwordEncoder.getClass());

	    UserResponse response = new UserResponse();
	    response.setUser(userMapper.toDTO(user));
	    
	    logger.info("### Utente recuperato con successo ###");
	    return response;
	}
	
}
