package com.efioco.ticketsystem.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_attachments")
public class TicketAttachmentEntity {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String filename; // es: "screenshot_errore.pdf"

    @Column(nullable = false)
    private String contentType; // es: "application/pdf" o "image/png"

    @Lob
    @Column(nullable = false, columnDefinition = "BYTEA")
    private byte[] content; // ✅ salvi il contenuto binario del file

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private TicketMessageEntity message; // ogni allegato appartiene a un messaggio

    public TicketAttachmentEntity() {
    }

    public TicketAttachmentEntity(String filename, String contentType, byte[] content) {
        this.filename = filename;
        this.contentType = contentType;
        this.content = content;
    }

    // 🔽 getter e setter
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; }

    public TicketMessageEntity getMessage() { return message; }
    public void setMessage(TicketMessageEntity message) { this.message = message; }
}

