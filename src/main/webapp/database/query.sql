CREATE DATABASE exercicioproceduremaven
USE exercicioproceduremaven

CREATE TABLE cliente(
cpf					CHAR(11)			NOT NULL,
nome				VARCHAR(100)		NOT NULL,
email				VARCHAR(200)		NOT NULL,
limite_de_credito	DECIMAL(7, 2)		NOT NULL,
dt_nascimento		DATE				NOT NULL
PRIMARY KEY (cpf))

CREATE PROCEDURE sp_cpf(@cpf CHAR(11), @cpf_valido BIT OUTPUT)
AS
	DECLARE @cont INT,
			@primeiro_digito INT,
			@segundo_digito INT
	SET @cpf_valido = CAST(0 AS BIT) 
	SET @primeiro_digito = 0
	SET @segundo_digito = 0
	-- Verificar se todos os números são iguais
	SET @cont = 2
	WHILE (@cont <= 11)
	BEGIN
		IF SUBSTRING(@cpf, 1 ,1) != SUBSTRING(@cpf, @cont ,1)
		BEGIN
			BREAK
		END
		IF @cont = 11
		BEGIN
			SET @cpf_valido = CAST(1 AS BIT) 
			RETURN @cpf_valido
		END
		SET @cont = @cont + 1
	END

	-- Calculo do primeiro digito
	SET @cont = 10
	WHILE (@cont >= 2)
	BEGIN
		SET @primeiro_digito = @primeiro_digito + (CAST(SUBSTRING(@cpf, (11 - @cont),1) AS INTEGER) * @cont)
		SET @cont = @cont - 1
	END
	SET @primeiro_digito = @primeiro_digito % 11
	IF @primeiro_digito < 2
	BEGIN
		SET @primeiro_digito = 0
	END
	ELSE
	BEGIN
		SET @primeiro_digito = 11 - @primeiro_digito
	END
	-- Calculo do segundo digito
	SET @cont = 11
	WHILE (@cont >= 2)
	BEGIN
		SET @segundo_digito = @segundo_digito + (CAST(SUBSTRING(@cpf, (12 - @cont),1) AS INTEGER) * @cont)
		SET @cont = @cont - 1
	END
	SET @segundo_digito = @segundo_digito % 11
	IF @segundo_digito < 2
	BEGIN
		SET @segundo_digito = 0
	END
	ELSE
	BEGIN
		SET @segundo_digito = 11 - @segundo_digito
	END
	-- Verificar se digitos batem com o que o usuario informou
	IF @primeiro_digito != SUBSTRING(@cpf, 10 ,1) OR  @segundo_digito != SUBSTRING(@cpf, 11 ,1)
	BEGIN
		SET @cpf_valido = CAST(1 AS BIT) 
	END
	RETURN @cpf_valido



CREATE PROCEDURE sp_iud_cliente (@op CHAR(1), @cpf CHAR(11), @nome VARCHAR(100), 
							@email VARCHAR(200), @limite_de_credito DECIMAL(7, 2),
							@dt_nascimento DATE, @saida VARCHAR(100) OUTPUT)
AS
	DECLARE @cpf_valido BIT
	EXEC sp_cpf @cpf, @cpf_valido OUTPUT
	
	IF (UPPER(@op) = 'I' AND @cpf_valido = 0)
	BEGIN
		INSERT INTO cliente VALUES (@cpf, @nome, @email, @limite_de_credito, @dt_nascimento)
		SET @saida = 'Cliente inserido com sucesso'
	END
	ELSE
	BEGIN
		IF (UPPER(@op) = 'U' AND @cpf_valido = 0)
		BEGIN
			UPDATE cliente SET nome = @nome, email = @email, limite_de_credito = @limite_de_credito, dt_nascimento = @dt_nascimento WHERE cpf = @cpf
			SET @saida = 'Cliente atualizado com sucesso'
		END
		ELSE
		BEGIN
			IF (UPPER(@op) = 'D')
			BEGIN
				DELETE cliente WHERE cpf = @cpf
				SET @saida = 'Cliente deletado com sucesso'
			END
			ELSE
			BEGIN
				IF (@cpf_valido = 1)
				BEGIN
					RAISERROR('CPF inválido', 16, 1)
				END
				ELSE
				BEGIN
					RAISERROR('Operação inválida', 16, 1)
				END
			END
		END
	END
