## Desafio Creditas Auto

### Subindo o ambiente
Para subir a aplicação:

* Estando na pasta raiz do projeto:
```sh
$ cd src/main/config/docker
$ docker-compose create
$ docker-compose start
```
* Dessa forma, a aplicação sobe localmente na porta 8090 e o banco na porta 3307.
* Usuário do banco = root | senha = secret-pw
* Arquivo swagger com a documentação das chamadas REST no root do projeto.
