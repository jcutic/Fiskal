package hr.fiskalizacija.exception;

/**
 * Oznaèava generalne iznimne dogaðaje prilikom izvršavanja procesa fiskalizacije.
 * 
 * @author aradovan
 *
 */
public class FiskalizacijaException extends RuntimeException {

	private static final long serialVersionUID = -5523656667530191812L;
	
	/**
	 * Služi za kreiranje objekta iznimke koji je opisan samo porukom.
	 * 
	 * @param message poruka vezana uz iznimku koja se kreira
	 */
	public FiskalizacijaException(String message) {
		super(message);
	}

	/**
	 * Služi za kreiranje objekta iznimke koji je opisan porukom i uzroènom iznimkom.
	 * 
	 * @param message poruka vezana uz iznimku koja se kreira
	 * @param cause uzroèna iznimka
	 */
	public FiskalizacijaException(String message, Throwable cause) {
		super(message, cause);
	}

}
