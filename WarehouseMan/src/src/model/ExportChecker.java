package src.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;

public class ExportChecker {
	@DAttr(name = "id", id = true, auto = true, type = DAttr.Type.Integer, mutable = false, optional = false, length = 6)
	private Integer id;
	private static int idCounter;

	@DAttr(name = "name", type = DAttr.Type.String, optional = false, length = 20)
	private String name;
	@DAttr(name = "phone", type = DAttr.Type.String, optional = false, length = 15)
	private String phone;
	@DAttr(name = "email", type = DAttr.Type.String, optional = false, length = 20)
	private String email;
	@DAttr(name = "address", type = Type.Domain, length = 20, optional = true)
	@DAssoc(ascName = "exportchecker-has-address", role = "exportchecker", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = Address.class, cardMin = 1, cardMax = 10))
	private Address address;

	@DOpt(type = DOpt.Type.ObjectFormConstructor)

	public ExportChecker(@AttrRef("name") String name, @AttrRef("phone") String phone, @AttrRef("email") String email,
			@AttrRef("address") Address address) {
		this(null, name, phone, email, address);
	}

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public ExportChecker(Integer id, String name, String phone, String email, Address address) {
		this.id = nextID(id);
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;

	}

	public Integer getId() {
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

	private static int nextID(Integer currID) {
		if (currID == null) {
			idCounter++;
			return idCounter;
		} else {
			int num = currID.intValue();
			if (num > idCounter)
				idCounter = num;

			return currID;
		}
	}

	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
			throws ConstraintViolationException {

		if (minVal != null && maxVal != null) {

			int maxIdVal = (Integer) maxVal;
			if (maxIdVal > idCounter) {
				idCounter = maxIdVal;
			}
		}
	}

}
