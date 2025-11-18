export class AuthError extends Error {
  constructor(public override message: string) {
    super(message);        // richiama il costruttore di Error
    this.name = 'AuthError';
    Object.setPrototypeOf(this, AuthError.prototype); // necessario per corretto instanceof
  }

  override toString() {
    return `Errore: ${this.message}`;
  }
}