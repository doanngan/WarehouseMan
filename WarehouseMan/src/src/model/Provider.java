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
import domainapp.basics.util.Tuple;

@DClass(schema="Warehouseman")
public class Provider {
	  public static final String P_id = "id";
	  public static final String P_name = "name";
	  public static final String P_phone = "phone";
	  public static final String P_email = "email";
	  public static final String P_address = "address";
	  public static final String P_rptProviderByName = "rptProviderByName";

	@DAttr(name = P_id, id = true, auto = true, type = Type.String, mutable = false, optional = false, length = 6)
	private String id;
	private static int idCounter = 0;

	@DAttr(name = P_name, type = Type.String, optional = false, length = 20)
//	@DAssoc(ascName = "Providers-by-name-report-has-Provider", role = "provider",
//	ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = Provider.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private String name;
	
	@DAttr(name = P_phone, type = Type.String, optional = false, length = 15)
	private String phone;
	
	@DAttr(name = P_email, type = Type.String, optional = false, length = 20)
	private String email;
	
	@DAttr(name = P_address, type = Type.Domain, length = 20, optional = true)
	@DAssoc(ascName = "provider-has-address", role = "provider", ascType = AssocType.One2Many, 
	        endType = AssocEndType.Many, associate = @Associate(type = Address.class, cardMin = 1, cardMax = 10))
	private Address address;
	
	@DAttr(name=P_rptProviderByName,type=Type.Domain, serialisable=false,
		   virtual=true)
	private ProvidersByNameReport rptProviderByName;
	
//	private Collection<ProvidersByNameReport> ProvidersByNameReport;
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	  public Provider(@AttrRef("name") String name, 
	      @AttrRef("phone") String phone, 
	      @AttrRef("address") Address address, 
	      @AttrRef("email") String email) {
	    this(null, name, phone, address, email);
	  }
	
	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public Provider(@AttrRef("id") String id, @AttrRef("name") String name, 
		      @AttrRef("phone") String phone, @AttrRef("address") Address address, 
		      @AttrRef("email") String email)
		    		  throws ConstraintViolationException {
		this.id = nextID(id);
		this.name = name;
	    this.phone = phone;
	    this.address = address;
	    this.email = email;
//	    ProvidersByNameReport = new ArrayList<>();
//		count = 0;
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
	