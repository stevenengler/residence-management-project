package lakehead.grouptwo.residence_management_system.data.identifiers;
//
import java.io.Serializable;
//
public class ID implements Serializable{
	private static final long serialVersionUID = -2264084609867561410L;
	//
	public final long id;
	//
	public ID(long _id){
		id = _id;
	}
}