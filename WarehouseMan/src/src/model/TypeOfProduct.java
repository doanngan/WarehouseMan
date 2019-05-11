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
import src.model.ProductsByTypeReport;

@DClass(schema="WarehouseMan")
public class TypeOfProduct {
	public static final String B_id = "id";
	public static final String B_name = "typeOfProduct";
	public static final String B_rptProductByType = "rptProductByType";

	@DAttr(name = B_id, id = true, auto = true, type = Type.String, length = 6, mutable = false, optional = false)
	private String id;
	private static int idCounter = 0;

	@DAttr(name = B_name, type = Type.String, length = 30, optional = false)
	private String typeOfProduct;
	
	@DAttr(name=B_rptProductByType,type=DAttr.Type.Domain, serialisable=false,
			   virtual=true)
		private ProductsByTypeReport rptProductByType;
	
	@DOpt(type=DOpt.Type.ObjectFormConstructor)
	@DOpt(type=DOpt.Type.RequiredConstructor)
	  public TypeOfProduct(@AttrRef("typeOfProduct") String typeOfProduct)   {
	    this(null, typeOfProduct);
	  }
	
	@DOpt(type=DOpt.Type.DataSourceConstructor)
	  public TypeOfProduct(@AttrRef("id") String id, 
	      @AttrRef("typeOfProduct") String typeOfProduct) 
	  throws ConstraintViolationException {
	    // generate an id
	    this.id = nextID(id);

	    // assign other values
	    this.typeOfProduct = typeOfProduct;
	}

	public String getId() {
		return id;
	}

	public String getTypeOfProduct() {
		return typeOfProduct;
	}

	public void setTypeOfProduct(String typeOfProduct) {
		this.typeOfProduct = typeOfProduct;
	}

	public ProductsByTypeReport getRptProductByType() {
	    return rptProductByType;
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
	    TypeOfProduct other = (TypeOfProduct) obj;
	    if (id == null) {
	      if (other.id != null)
	        return false;
	    } else if (!id.equals(other.id))
	      return false;
	    return true;
	  }
	
//	@Override
//	public String toString() {
//		return "typeOfProduct (" + id + "," + typeOfProduct + " )";
//	}

	// automatically generate the next student id
//	 private String nextID(String id) throws ConstraintViolationException {
//		    if (id == null) { // generate a new id
//		        idCounter++;
//		      return "T" + idCounter;
//		    } else {
//		      // update id
//		      int num;
//		      try {
//		        num = Integer.parseInt(id.substring(1));
//		      } catch (RuntimeException e) {
//		        throw new ConstraintViolationException(
//		            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
//		      }
//		      
//		      if (num > idCounter) {
//		        idCounter = num;
//		      }
//		      
//		      return id;
//		    }
//		  }

	private String nextID(String id) throws ConstraintViolationException {
	    if (id == null) { // generate a new id
	      if (idCounter == 0) {
	        idCounter = Calendar.getInstance().get(Calendar.YEAR);
	      } else {
	        idCounter++;
	      }
	      return "T" + idCounter;
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
	
		  /**
		   * @requires 
		   *  minVal != null /\ maxVal != null
		   * @effects 
		   *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
		   */
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