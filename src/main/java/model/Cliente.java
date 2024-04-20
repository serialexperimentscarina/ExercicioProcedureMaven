package model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {
	
	private String cpf;
	private String nome;
	private String email;
	private double limiteDeCredito;
	private Date dtNascimento;

}
