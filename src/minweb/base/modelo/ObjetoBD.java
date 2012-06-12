package minweb.base.modelo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ObjetoBD {
	@Id
	@Column(nullable=false)
	@GeneratedValue
	private Integer id;
	
	public int getId() { return id; }
}