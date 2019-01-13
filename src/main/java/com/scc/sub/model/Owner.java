package com.scc.sub.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ws_dog_proprietaire")
public class Owner implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	private int id;

	@Column(name = "nom")
	private String lastName;

	@Column(name = "prenom")
	private String firstName;

	@Column(name = "adresse")
	private String address;

	@Column(name = "code_postal")
	private String zipCode;

	@Column(name = "ville")
	private String town;

	@Column(name = "pays")
	private String country;

	@Id
	@Column(name = "id_chien", nullable = false)
	private int idDog;

	@Column(name = "date_maj")
	private Timestamp timestamp;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	public String getZipCode() { return zipCode; }
	public void setZipCode(String zipCode) { this.zipCode = zipCode; }

	public String getTown() { return town; }
	public void setTown(String town) { this.town = town; }

	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }

	public int getIdDog() { return idDog; }
	public void setIdDog(int idDog) { this.idDog = idDog; }

	public Timestamp getTimestamp() { return timestamp; }
	public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

	public Owner withId(int id){ this.setId( id ); return this; }
	public Owner withFirstName(String firstName){ this.setFirstName(firstName); return this; }
	public Owner withLastName(String lastName){ this.setLastName(lastName); return this; }
	public Owner withAddress(String address){ this.setAddress(address); return this; }
	public Owner withZipCode(String zipCode){ this.setZipCode(zipCode); return this; }
	public Owner withTown(String town){ this.setTown(town); return this; }
	public Owner withCountry(String country){ this.setCountry(country); return this; }
	public Owner withIdDog(int idDog){ this.setIdDog(idDog); return this; }
	public Owner withTimestamp(Timestamp timestamp){ this.setTimestamp(timestamp); return this; }

	@Override
	public String toString() {
		return "Owner [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", address=" + address
				+ ", zipCode=" + zipCode + ", town=" + town + ", country=" + country + ", idDog=" + idDog
				+ ", timestamp=" + timestamp + "]";
	}

}
