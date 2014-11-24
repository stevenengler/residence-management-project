package lakehead.grouptwo.residence_management_system.data;

//
public class AuthenticationException extends Exception{
	private static final long serialVersionUID = 7283071084951458740L;
	//
	public AuthenticationException(String message){
        super(message);
    }
	public AuthenticationException(String message, Exception e){
        super(message, e);
    }
}