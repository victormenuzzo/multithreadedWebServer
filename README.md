# multithreadedWebServer

Made by Victor Antonio Menuzzo and Paula Giovanna Rodrigues

Para rodar no linux abrir o terminal na pasta e dar o comando:

~$make


Para testar deixar o programa rodando e digitar a seguinte linha no browser:

http://IP_DO_PC_COM_O_CODIGO:PORTA_USADA/ARQUIVO_A_SER_RODADO


Onde:

IP_DO_PC_COM_O_CODIGO - IP DO COMPUTADOR QUE ESTÁ RODANDO O WebServer (para descobrir seu ip no linux: ~$ ip addr)


PORTA_USADA - PORTA PRÉ ESTABELECIDA NO WebServer.java (int port)


ARQUIVO_A_SER_RODADO - UM DOS ARQUIVOS DEFINIDOS NA PASTA (gatinho.gif ou pantera.jpg)


Para visualizar a resposta do HTTP dar o comando: 

~$ curl -I http://IP_DO_PC_COM_O_CODIGO:PORTA_USADA/ARQUIVO_A_SER_RODADO
