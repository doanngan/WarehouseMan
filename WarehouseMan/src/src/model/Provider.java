package src.model;

import java.util.Calendar;
import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import src.model.ProvidersByNameReport;

@DClass(schema="WarehouseMan")
public class Provider {
	  public static final String A_name = "name";
	  public static final String A_id = "id";
	  public static final String A_phone = "phone";
	  public static final String A_address = "address";
	  public static final String A_email = "email";
	  public static final String A_rptProviderByName = "rptProviderByName";

	@DAttr(name = A_id, id = true, auto = true, type = Type.String, mutable = false, optional = false, length = 6)
	private String id;
	private static int idCounter = 0;

	@DAttr(name = A_name, type = Type.String, optional = false, length = 20)
	private String name;
	@DAttr(name = A_phone, type = Type.String, optional = false, length = 15)
	private String phone;
	@DAttr(name = A_email, type = Type.String, optional = false, length = 20)
	private String email;
	@DAttr(name = A_address, type = Type.Domain, length = 20, optional = true)
	@DAssoc(ascName = "provider-has-address", role = "provider", ascType = AssocType.One2Many, 
	        endType = AssocEndType.Many, associate = @Associate(type = Address.class, cardMin = 1, cardMax = 10))
	private Address address;
	
	@DAttr(name=A_rptProviderByName,type=DAttr.Type.Domain, serialisable=false,
		   virtual=true)
	private ProvidersByNameReport rptProviderByName;
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	  public Provider(@AttrRef("name") String name, 
	      @AttrRef("phone") String phone, 
	      @AttrRef("address") Address address, 
	      @AttrRef("email") String email) {
	    this(null, name, phone, address, email);
	  }
	
//	@DOpt(type=DOpt.Type.ObjectFormConstructor)
//	  public Provider(@AttrRef("name") String name, 
//	      @AttrRef("phone") String phone, 
//	      @AttrRef("address") Address address, 
//	      @AttrRef("email") String email) {
//	    this(null, name, dob, address, email, sclass);
//	  }

	@DOpt(type=DOpt.Type.DataSourceConstructor)
	  public Provider(@AttrRef("id") String id, 
	      @AttrRef("name") String name, @AttrRef("phone") String phone, @AttrRef("address") Address address, 
	      @AttrRef("email") String email) 
	  throws ConstraintViolationException {
	    // generate an id
	    this.id = nextID(id);

	    // assign other values
	    this.name = name;
	    this.phone = phone;
	    this.address = address;
	    this.email = email;
	}
	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public ProvidersByNameReport getRptProviderByName() {
	    return rptProviderByName;
	  }

	@Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    Provider other = (Provider) obj;
	    if (id == null) {
	      if (other.id != null)
	        return false;
	    } else if (!id.equals(other.id))
	      return false;
	    return true;
	  }
//	private static int nextID(Integer currID) {
//		if (currID == null) {
//			idCounter++;
//			return idCounter;
//		} else {
//			int num = currID.intValue();
//			if (num > idCounter)
//				idCounter = num;
//
//			return currID;
//		}
//	}

	private String nextID(String id) throws ConstraintViolationException {
	    if (id == null) { // generate a new id
	      if (idCounter == 0) {
	        idCounter = Calendar.getInstance().get(Calendar.YEAR);
	      } else {
	        idCounter++;
	      }
	      return "P" + idCounter;
	    } else {
	      // update id
	      int num;
	      try {
	        num = Integer.parseInt(id.substring(1));
	      } catch (RuntimeException e) {
	        throw new ConstraintViolationException(
	            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
	      }
	      
	      if (num > idCounter) {
	        idCounter = num;
	      }
	      
	      return id;
	    }
	  }
	
//	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
//	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
//			throws ConstraintViolationException {
//
//		if (minVal != null && maxVal != null) {
//
//			int maxIdVal = (Integer) maxVal;
//			if (maxIdVal > idCounter) {
//				idCounter = maxIdVal;
//			}
//		}
//	}
//
//}
	
	@DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
	  public static void updateAutoGeneratedValue(
	      DAttr attrib,
	      Tuple derivingValue, 
	      Object minVal, 
	      Object maxVal) throws ConstraintViolationException {
	    
	    if (minVal != null && maxVal != null) {
	      //TODO: update this for the correct attribute if there are more than one auto attributes of this class 

	      String maxId = (String) maxVal;
	      
	      try {
	        int maxIdNum = Integer.parseInt(maxId.substring(1));
	        
	        if (maxIdNum > idCounter) // extra check
	          idCounter = maxIdNum;
	        
	      } catch (RuntimeException e) {
	        throw new ConstraintViolationException(
	            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] {maxId});
	      }
	    }
	  }
	}
