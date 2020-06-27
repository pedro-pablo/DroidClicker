# DroidClicker

### Aluno: Pedro Pablo

DroidClicker é um jogo do tipo clicker onde o jogador deve fabricar e vender produtos a fim de obter lucro e alcançar uma pontuação alta.

O jogador poderá contratar funcionários e desbloquear melhorias para aprimorar o processo produtivo da empresa. Conforme o jogo progride,
o preço de venda dos produtos deverá ser equilibrado para a empresa não vender no prejuízo, ficar com estoque cheio ou não conseguir atender a demanda.

## Visão geral
O software se trata de um jogo mobile onde o jogador é responsável por administrar uma fábrica fictícia. 
O objetivo principal é fabricar e vender produtos para alcançar a maior pontuação que conseguir, 
ajustando o preço de venda e comprando melhorias para aumentar os lucros e reduzir o trabalho necessário para fabricar produtos. 
Os jogadores do mesmo dispositivo poderão comparar as pontuações obtidas, que são computadas quando 
o jogador escolher “encerrar as operações” da empresa fictícia. É necessário cadastrar uma conta local com nome e senha para ter acesso ao jogo.

## Requisitos funcionais
* **(RF01)** Criação de conta: O jogo deverá permitir que o usuário cadastre uma conta informando nome e senha.
* **(RF02)** Hashing da senha: O jogo deverá salvar no banco de dados a hash da senha do usuário.
* **(RF03)** Exibir informações da empresa fictícia na tela principal do jogo: O jogo deverá exibir ao jogador informações relevantes sobre a empresa fictícia.
* **(RF04)** Exibir informações detalhadas em outra tela: O jogo deverá exibir informações detalhadas da empresa fictícia em outra tela.
* **(RF05)** Ajuste de preço: O jogo deverá permitir que o jogador altere o preço atual de venda do produto fictício.
* **(RF06)** Aquisição de melhorias: O jogo deverá permitir que o jogador use moeda fictícia para aquisição de melhorias, a fim de obter vantagens na atividade principal do jogo.
* **(RF07)** Demanda dinâmica: O jogo deverá implementar demanda dinâmica para o produto fictício a venda, baseado na oferta de produtos e a proporção entre um valor de mercado gerado aleatoriamente (baseado no custo atual de produção) e o preço de venda atual.
* **(RF08)** Manter progressão do jogo: O jogo deverá guardar todos os dados relacionados à sessão atual do jogo caso o usuário decida sair do aplicativo. Quando o jogador entrar em sua conta novamente, os dados salvos deverão ser restaurados. Dessa forma, o progresso do jogo atual é mantido.
* **(RF09)** Finalizar o jogo atual: O jogo deverá permitir que o jogador “encerre as operações” da empresa fictícia, efetivamente finalizando o jogo e gerando a pontuação final.
* **(RF10)** Salvar pontuação: O jogo deverá computar a pontuação final obtida pelo jogador no decorrer do jogo atual após sua finalização. A pontuação será salva no banco de dados local, onde estará vinculada à conta autenticada.
* **(RF11)** Exibir todas pontuações: O jogo deverá exibir todas as pontuações obtidas por todos os usuários do mesmo dispositivo.
* (RF12) Metas: O jogo deverá ter metas de progressão que, ao serem atingidas pelo jogador, aumentarão a pontuação final.
* (RF13) Exibir as metas: O jogo deverá exibir informações sobre cada meta em uma tela separada da tela principal do jogo.
* **(RF14)** Sons: O jogo deverá emitir sons para as seguintes ações: contratar funcionário, produção feita por funcionário, atingir uma meta e encerrar as operações.

