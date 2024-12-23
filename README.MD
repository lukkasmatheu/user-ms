### Desafio Tecnico VM-TECNOLOGIA

Aplicação foi desenvolvida em java 21, utilizando :

* spring boot-webmvc
* Spring-security
* mongo-db
* junit5


Para realizar o uso das api é necessario realizar a autenticação basica.
* login:admin
* senha:vm_password


### Testes Unitarios
Cobertura de testes
Foram criados testes unitarios nos seguintes fluxos Service/Components/Repository visando a cobertura de todos os metodos
e classes, os testes buscaram explorar casos de falhas (Exceções) e o comportamento
normal que os metodos deveriam seguir.
Foram criados testes de API Controller - para validar entrada de dados e tratamento
das exceções pelo controller advice.

### Proposta de FrontEnd
Para antender ao proposito da aplicação poderia ser desenvolvido
um front-end com vue ou react, o react permitindo componentização 
dos elementos podendo ser replicados em outras telas e centralizando manutenção
e o vue por ser leve e poder ser integrado com mais facilidade em uma aplicação ja existente,
ambos possui o ponto fraco de ter uma curva de aprendizado em relação a gerenciamento de memoria
ciclo de vida de componentes e estruturação e organização do projeto que pode levar a 
geração de um monolito se mal organizado.