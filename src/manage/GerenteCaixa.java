/**
 * File:Gerente.java
 */
package manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import util.Garcon;
import util.ItemDoRestaurante;
import util.Pedido;
import util.Produto;
import arquivos.ManipulaArquivo;

/* Aluno: Carlos Alberto de Amorim Porto - 121085031  */

/**
 * 
 * Gerente que controla o caixa do restaurante.
 * 
 * @author carlos
 *
 */
public class GerenteCaixa {

	// Total de dinheiro disponivel no Caixa.
	private double dinheiroEmCaixa, totalArrecadado;
	// Estado do Caixa.
	private boolean caixaAberto;

	private Scanner entrada;
	private ManipulaArquivo arquivador;

	private List<ItemDoRestaurante> listaDeProdutosCadastrados;
	private List<ItemDoRestaurante> listaDeGarconsDoRestaurante;
	private List<String> registrosDeMovimentacao;
	private Map<String, Integer> produtosConsumidos;

	private final String PROMPT_GERAL = System.lineSeparator()
			+ "Caixa de Restaurante" + System.lineSeparator()
			+ "|_ 1 - Gerenciar produtos;" + System.lineSeparator()
			+ "|_ 2 - Gerenciar garcons;" + System.lineSeparator()
			+ "|_ 3 - Abrir Caixa;" + System.lineSeparator()
			+ "|_ 4 - Movimentar Caixa;" + System.lineSeparator()
			+ "|_ 5 - Balanco;" + System.lineSeparator()
			+ "|_ 6 - Relatorios;" + System.lineSeparator()
			+ "|_ 7 - Fechar Caixa e Sair." + System.lineSeparator()
			+ "Opcao desejada: ";
	private final String PROMPT_OPCAO_1 = System.lineSeparator()
			+ "Gerenciar produto" + System.lineSeparator()
			+ "|_ 1 - Cadastrar produto;" + System.lineSeparator()
			+ "|_ 2 - Editar produto;" + System.lineSeparator()
			+ "|_ 3 - Excluir produto;" + System.lineSeparator()
			+ "|_ 4 - Voltar." + System.lineSeparator()
			+ "Opcao desejada: ";
	private final String PROMPT_OPCAO_2 = System.lineSeparator()
			+ "Gerenciar garcons" + System.lineSeparator()
			+ "|_ 1 - Cadastrar garcon;" + System.lineSeparator()
			+ "|_ 2 - Editar garcon;" + System.lineSeparator()
			+ "|_ 3 - Excluir garcon;" + System.lineSeparator()
			+ "|_ 4 - Voltar." + System.lineSeparator()
			+ "Opcao desejada: ";

	private final String PROMPT_OPCAO_4 = System.lineSeparator()
			+ "Movimento do Caixa" + System.lineSeparator()
			;
	private final String PROMPT_OPCAO_6 = System.lineSeparator()
			+ "Relatorios" + System.lineSeparator()
			+ "|_ 1 - Listar produtos;" + System.lineSeparator()
			+ "|_ 2 - Listar Garcons;" + System.lineSeparator()
			+ "|_ 3 - Produtos mais/menos consumidos;" + System.lineSeparator()
			+ "|_ 4 - Gorjetas acumuladas;" + System.lineSeparator()
			+ "|_ 5 - Voltar." + System.lineSeparator()
			+ "Opcao desejada: ";

	public GerenteCaixa(){
		this.setDinheiroEmCaixa(0.0);
		this.setCaixaAberto(false);

		entrada = new Scanner(System.in);
		arquivador = new ManipulaArquivo();

		listaDeProdutosCadastrados = arquivador.carregaListaDeProdutos();
		listaDeGarconsDoRestaurante = arquivador.carregaListaDeGarcons();
		registrosDeMovimentacao = new ArrayList<String>();
		produtosConsumidos = new HashMap<String, Integer>();
	}

	/**
	 * Modifica o estado do caixa.
	 * 
	 * @param estado true para abrir o caixa e false para fecha-lo
	 */
	private void setCaixaAberto(boolean estado) {
		this.caixaAberto = estado;
	}

	/**
	 * Modifica o total de dinheiro disponível no caixa.
	 * 
	 * @param dinheiro Total de dinheiro no caixa.
	 */
	private void setDinheiroEmCaixa(double dinheiro) {
		if(dinheiro >=0)
			this.dinheiroEmCaixa += dinheiro;
	}

	/**
	 * Modulo de gerencia de produtos, responsável por cadastrar produtos na cozinha,
	 * editar e remover produtos da mesma.
	 */
	private void gerenciaProduto(){
		System.out.println(PROMPT_OPCAO_1);
		int opcao1 = entrada.nextInt();
		while(opcao1 != 4){
			switch(opcao1){
			case 1:
				Cozinha.cadastrarProduto(listaDeProdutosCadastrados);
				break;
			case 2:
				Cozinha.editarProduto(listaDeProdutosCadastrados);
				break;
			case 3:
				Cozinha.excluirProduto(listaDeProdutosCadastrados);
				break;
			default:
				System.out.println("Opcao invalida.");
				break;
			}// fim do switch
			System.out.println(PROMPT_OPCAO_1);
			opcao1 = entrada.nextInt();
		}// fim do while
	}

	/**
	 * Modulo de gerencia de garcons. Responsável por cadastrar garcons no restaurante,
	 * editar e remover garcons do mesmo.
	 */
	private void gerenciaGarcon(){
		System.out.println(PROMPT_OPCAO_2);
		int opcao2 = entrada.nextInt();
		while(opcao2 != 4){
			switch(opcao2){
			case 1:
				Escritorio.cadastrarGarcon(listaDeGarconsDoRestaurante);
				break;
			case 2:
				Escritorio.editarGarcon(listaDeGarconsDoRestaurante);
				break;
			case 3:
				Escritorio.excluirGarcon(listaDeGarconsDoRestaurante);
				break;
			default:
				System.out.println("Opcao invalida.");
				break;
			}// fim do switch
			System.out.println(PROMPT_OPCAO_2);
			opcao2 = entrada.nextInt();
		}// fim do while
	}

	/**
	 * Abre o caixa do restaurante adicionando o quanto de dinheiro estará disponível.
	 */
	private void abreCaixa(){
		boolean sucesso = false;
		while(!sucesso){
			System.out.println("Abrindo Caixa...");
			System.out.println("Digite a quantidade de dinheiro no Caixa: ");
			double dinheiro = Double.parseDouble(entrada.next());
			if(dinheiro > 0.0){
				this.setDinheiroEmCaixa(dinheiro);;
				this.setCaixaAberto(true);
				sucesso = true;
				System.out.println("Caixa aberto com sucesso!");
			}
		}
	}

	/**
	 * Movimenta o caixa do restaurante processando as contas dos clientes.
	 */
	private void movimentaCaixa(){
		if(caixaAberto){
			System.out.println(PROMPT_OPCAO_4);
			Pedido pedido;
			boolean temMaisPedidos = true;
			while(temMaisPedidos){
				pedido = recebePedido();
				pagaConta(pedido);
				System.out.println("Mais algum pedido (s/n)? ");
				String resposta = entrada.next();
				if(resposta.equals("n")){
					temMaisPedidos = false;
				}
			}
		}
		else
			System.out.println("Caixa fechado. Abra o Caixa antes de movimenta-lo.");
	}

	/**
	 * Recebe todas as informacoes referentesao pedido do cliente.
	 * @return O pedido do cliente
	 */
	private Pedido recebePedido(){
		Pedido pedido;
		List<Produto> produtosDoPedido = new ArrayList<Produto>();
		System.out.println("Pedido No: ");
		int numeroDoPedido = entrada.nextInt();
		System.out.println("Codigo do garcon: ");
		int codigoGarcon = entrada.nextInt();
		Garcon garcon = new Garcon(codigoGarcon, "");
		System.out.println(listaDeGarconsDoRestaurante.get(listaDeGarconsDoRestaurante.indexOf(garcon)));
		int temProduto = 0;
		while(temProduto != -1){
			System.out.println("Codigo do produto: ");
			int codigo = entrada.nextInt();
			produtosDoPedido.add((Produto) listaDeProdutosCadastrados.get(listaDeProdutosCadastrados.indexOf(new Produto(codigo,"", 0.0))));

			System.out.println("Mais algum produto (s/n)? ");
			String resposta = entrada.next();
			if(resposta.equals("n")){
				temProduto = -1;
			}
		}
		pedido = new Pedido(numeroDoPedido, codigoGarcon,  produtosDoPedido.toArray(new Produto[produtosDoPedido.size()]));
		return pedido;

	}


	/**
	 * Processa o pedido do cliente pagando a conta e gerando os relatorios.
	 * @param pedido O pedido a ser processado
	 */
	private void pagaConta(Pedido pedido){
		double subtotal = 0.0;
		Produto[] listaDeProdutos = pedido.getProdutosConsumidos();
		for(int i = 0; i < listaDeProdutos.length; i++){
			subtotal += listaDeProdutos[i].getPreco();
		}
		Garcon garcon = (Garcon) listaDeGarconsDoRestaurante.get(listaDeGarconsDoRestaurante.indexOf(new Garcon(pedido.getCodigoDoGarcon(), "")));
		double gorjeta = subtotal/10;
		garcon.setGorjeta(gorjeta);
		double total = subtotal + gorjeta;
		System.out.println(
				"Subtotal: R$ " + subtotal + System.lineSeparator() +
				"Gorjeta: R$ " + gorjeta + System.lineSeparator() +
				"Total: R$ " + total);
		System.out.println(
				"Meio de pagamento:" + System.lineSeparator() +
				"1 - Dinheiro;" + System.lineSeparator() +
				"2 - Cheque;" + System.lineSeparator() +
				"3 - Cartao." + System.lineSeparator() +
				"Opcao: ");
		int opcao = entrada.nextInt();
		switch(opcao){
		case 1:
			System.out.println("Recebido: ");
			double dinheiro = entrada.nextDouble();
			double troco = dinheiro - total;
			if(troco > dinheiroEmCaixa){
				System.out.println("Dinheiro em caixa insuficiente para troco.");
			}
			else if(troco < 0){
				System.out.println("Falta " + (total - dinheiro) + " para completar o pagamento." );
			}
			else{
				dinheiroEmCaixa += total;
				totalArrecadado += total;
				System.out.println("Troco: R$" + troco);
			}
			break;
		case 2:
			System.out.println("Pagamento em cheque.");
			dinheiroEmCaixa += total;
			totalArrecadado += total;
			break;
		case 3:
			System.out.println("Pagamento em cartao.");
			dinheiroEmCaixa += total;
			totalArrecadado += total;
			break;
		}
		// TODO balanco
		Date data = new Date();
		SimpleDateFormat dataFormatada = new SimpleDateFormat("d/M/y H:m");
		registrosDeMovimentacao.add(dataFormatada.format(data) + " - " + pedido.getNumero() + " R$ " + total);
	}

	/**
	 * Exibe o balanco do caixa ate o momento.
	 */
	private void balanco(){
		Iterator<String> iterador = registrosDeMovimentacao.iterator();
		while(iterador.hasNext()){
			System.out.println(iterador.next());
			System.out.println("Total arrecadado: " + totalArrecadado);
		}
	}

	/**
	 * Modulo que exibe os relatorios disponiveis.
	 */
	private void relatorios(){
		System.out.println(PROMPT_OPCAO_6);
		int opcao6 = entrada.nextInt();
		while(opcao6 != 5){
			switch(opcao6){
			case 1:
				Cozinha.listarProdutos(listaDeProdutosCadastrados);
				break;
			case 2:
				Escritorio.listarGarcons(listaDeGarconsDoRestaurante);
				break;
			case 3:
				consumoDosProdutos();
				break;
			case 4:
				gorjetas();
				break;
			default:
				System.out.println("Opcao invalida.");
				break;
			}// fim do switch
			System.out.println(PROMPT_OPCAO_6);
			opcao6 = entrada.nextInt();
		}// fim do while
	}

	/**
	 * Exibe os produtos mais e menos consumidos no restaurante.
	 */
	private void consumoDosProdutos(){
		Set<String> chaves = produtosConsumidos.keySet();
		Iterator<String> iterador = chaves.iterator();

		String produtoMaisConsumido = "";
		String produtoMenosConsumido = "";
		Integer maisConsumido = 0;
		Integer menosConsumido = Integer.MAX_VALUE;
		while(iterador.hasNext()){
			String entrada = iterador.next();
			if(produtosConsumidos.get(entrada) > maisConsumido){
				produtoMaisConsumido = entrada;
			}
			if(produtosConsumidos.get(entrada) < menosConsumido){
				produtoMenosConsumido = entrada;
			}
		}
		System.out.println("Produto mais consumido: " + produtoMaisConsumido + System.lineSeparator() + "Quantidade: " + maisConsumido + System.lineSeparator() + System.lineSeparator());
		System.out.println("Produto menos consumido: " + produtoMenosConsumido + System.lineSeparator() + "Quantidade: " + menosConsumido + System.lineSeparator() + System.lineSeparator());
	}

	/**
	 * Exibe as gorjetas acumuladas pelos garcons.
	 */
	private void gorjetas(){
		System.out.println("Codigo do garcon: ");
		int codigo = entrada.nextInt();
		ItemDoRestaurante garcon = new Garcon(codigo, "");
		if(listaDeGarconsDoRestaurante.contains(garcon)){
			garcon = listaDeGarconsDoRestaurante.get(listaDeGarconsDoRestaurante.indexOf(garcon));
			System.out.println(((Garcon) garcon).getCodigo() + " - " + ((Garcon) garcon).getNome() + System.lineSeparator()
					+ "Total de gorjetas acumuladas: " + ((Garcon) garcon).getGorjeta());
		}
		else
			System.out.println("Codigo nao existe.");
	}

	/**
	 * Fecha o caixa do restaurante.
	 */
	private void fechaCaixa(){
		System.out.println("Fechando caixa...");
		arquivador.salvaLista(listaDeProdutosCadastrados);
		arquivador.salvaLista(listaDeGarconsDoRestaurante);
		entrada.close();

		if(caixaAberto){	
			this.setCaixaAberto(false);
		}
		else
			System.out.println("Caixa não está aberto.");

		System.out.println("Caixa fechado. Saindo do sistema...");
	}

	/**
	 * Modulo principal que gerencia o funcionamento do restaurante.
	 */
	public void run() {

		// Menu principal
		System.out.println(PROMPT_GERAL);
		int opcao = entrada.nextInt();

		// Execucao do programa
		while(opcao != 7){
			switch(opcao){
			case 1:
				gerenciaProduto();
				break;
			case 2:
				gerenciaGarcon();
				break;
			case 3:
				abreCaixa();
				break;
			case 4:
				movimentaCaixa();
				break;
			case 5:
				balanco();
				break;
			case 6:
				relatorios();
				break;
			default:
				System.out.println("Opcao invalida.");
				break;
			}// fim do switch
			System.out.println(PROMPT_GERAL);
			opcao = entrada.nextInt();
		}// fim do while

		fechaCaixa();
	}
}
