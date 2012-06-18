package minweb.base.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ObjetoBD implements Serializable {
	private static final long serialVersionUID = -8186792048484445154L;
	
	@Id
	@Column(nullable=false)
	@GeneratedValue
	private Integer id;
	
	public int getId() { return id; }
}