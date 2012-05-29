ProjetoMinWeb
=============

Projeto da cadeira de mineração na web.

Configuração
=============

Instalar:
- Eclipse Java EE
- Apache Tomcat v7.0
- MySQL Server
- MySQL Workbench
- git (obvio não)

Da clone no projeto e importa pro eclipse.
Cria um servidor Tomcat e lembra de colocar o nome do Server Runtime como "Apache Tomcat v7.0" (o nome do servidor tanto faz, esse é o nome do SERVER RUNTIME)
Baixa o driver do mysql (MySQL Connector/J) e coloca na pasta lib do Tomcat.
Configura o DataSource do MySQL no arquivo "context.xml" do Tomcat.

<Resource factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
    		name="jdbc/MinWebDB"
    		auth="Container"
    		type="javax.sql.DataSource"
    		username="<username>"
    		password="<password>"
    		driverClassName="com.mysql.jdbc.Driver"
    		url="jdbc:mysql://localhost:3306/<banco>"/>

e priu. Have Fun!