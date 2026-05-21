package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

    @Autowired
    private EmpresaRepositorio repositorioEmpresa;

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Car Service Toyota Ltda");
        empresa.setNomeFantasia("Car Service Manutenção Veicular");
        empresa.setCadastro(new Date());

        Endereco enderecoEmpresa = new Endereco();
        enderecoEmpresa.setEstado("São Paulo");
        enderecoEmpresa.setCidade("São Paulo");
        enderecoEmpresa.setBairro("Centro");
        enderecoEmpresa.setRua("Av. São João");
        enderecoEmpresa.setNumero("00");
        enderecoEmpresa.setCodigoPostal("01035-000");
        empresa.setEndereco(enderecoEmpresa);

        Telefone telefoneEmpresa = new Telefone();
        telefoneEmpresa.setDdd("011");
        telefoneEmpresa.setNumero("986454527");
        empresa.getTelefones().add(telefoneEmpresa);

        // ======= ADMINISTRADOR =======
        Usuario admin = new Usuario();
        admin.setNome("Administrador do Sistema");
        admin.setNomeSocial("Admin");
        admin.getPerfis().add(PerfilUsuario.ROLE_ADMIN);

        CredencialUsuarioSenha credAdmin = new CredencialUsuarioSenha();
        credAdmin.setInativo(false);
        credAdmin.setNomeUsuario("admin");
        credAdmin.setSenha(codificador.encode("admin123"));
        credAdmin.setCriacao(new Date());
        credAdmin.setUltimoAcesso(new Date());
        admin.getCredenciais().add(credAdmin);
        empresa.getUsuarios().add(admin);

        // ======= GERENTE =======
        Usuario gerente = new Usuario();
        gerente.setNome("Pedro Alcântara de Bragança e Bourbon");
        gerente.setNomeSocial("Dom Pedro Gerente");
        gerente.getPerfis().add(PerfilUsuario.ROLE_GERENTE);

        Email emailGerente = new Email();
        emailGerente.setEndereco("gerente@toyota.com");
        gerente.getEmails().add(emailGerente);

        Endereco endGerente = new Endereco();
        endGerente.setEstado("São Paulo");
        endGerente.setCidade("São Paulo");
        endGerente.setBairro("Jardins");
        endGerente.setRua("Av. São Gabriel");
        endGerente.setNumero("00");
        endGerente.setCodigoPostal("01435-001");
        gerente.setEndereco(endGerente);

        Telefone telGerente = new Telefone();
        telGerente.setDdd("011");
        telGerente.setNumero("9854633728");
        gerente.getTelefones().add(telGerente);

        Documento cpfGerente = new Documento();
        cpfGerente.setDataEmissao(new Date());
        cpfGerente.setNumero("856473819229");
        cpfGerente.setTipo(TipoDocumento.CPF);
        gerente.getDocumentos().add(cpfGerente);

        CredencialUsuarioSenha credGerente = new CredencialUsuarioSenha();
        credGerente.setInativo(false);
        credGerente.setNomeUsuario("gerente");
        credGerente.setSenha(codificador.encode("gerente123"));
        credGerente.setCriacao(new Date());
        credGerente.setUltimoAcesso(new Date());
        gerente.getCredenciais().add(credGerente);
        empresa.getUsuarios().add(gerente);

        // ======= VENDEDOR =======
        Usuario vendedor = new Usuario();
        vendedor.setNome("Componentes Varejo de Partes Automotivas Ltda");
        vendedor.setNomeSocial("Vendedor");
        vendedor.getPerfis().add(PerfilUsuario.ROLE_VENDEDOR);

        Email emailVendedor = new Email();
        emailVendedor.setEndereco("vendedor@toyota.com");
        vendedor.getEmails().add(emailVendedor);

        CredencialUsuarioSenha credVendedor = new CredencialUsuarioSenha();
        credVendedor.setInativo(false);
        credVendedor.setNomeUsuario("vendedor");
        credVendedor.setSenha(codificador.encode("vendedor123"));
        credVendedor.setCriacao(new Date());
        credVendedor.setUltimoAcesso(new Date());
        vendedor.getCredenciais().add(credVendedor);

        Documento cnpj = new Documento();
        cnpj.setDataEmissao(new Date());
        cnpj.setNumero("00014556000100");
        cnpj.setTipo(TipoDocumento.CNPJ);
        vendedor.getDocumentos().add(cnpj);

        Endereco endVendedor = new Endereco();
        endVendedor.setEstado("Rio de Janeiro");
        endVendedor.setCidade("Rio de Janeiro");
        endVendedor.setBairro("Centro");
        endVendedor.setRua("Av. República do Chile");
        endVendedor.setNumero("00");
        endVendedor.setCodigoPostal("20031-170");
        vendedor.setEndereco(endVendedor);
        empresa.getUsuarios().add(vendedor);

        // ======= MERCADORIAS =======
        Mercadoria rodaLigaLeve = new Mercadoria();
        rodaLigaLeve.setCadastro(new Date());
        rodaLigaLeve.setFabricao(new Date());
        rodaLigaLeve.setNome("Roda de liga leve modelo Toyota Etios");
        rodaLigaLeve.setValidade(new Date());
        rodaLigaLeve.setQuantidade(30);
        rodaLigaLeve.setValor(300.0);
        rodaLigaLeve.setDescricao("Roda de liga leve original de fábrica Toyota para modelos hatch");
        empresa.getMercadorias().add(rodaLigaLeve);
        vendedor.getMercadorias().add(rodaLigaLeve);

        // ======= CLIENTE =======
        Usuario cliente = new Usuario();
        cliente.setNome("Pedro Alcântara de Bragança e Bourbon");
        cliente.setNomeSocial("Dom Pedro Cliente");
        cliente.getPerfis().add(PerfilUsuario.ROLE_CLIENTE);

        Email emailCliente = new Email();
        emailCliente.setEndereco("c@c.com");
        cliente.getEmails().add(emailCliente);

        Documento cpfCliente = new Documento();
        cpfCliente.setDataEmissao(new Date());
        cpfCliente.setNumero("12584698533");
        cpfCliente.setTipo(TipoDocumento.CPF);
        cliente.getDocumentos().add(cpfCliente);

        CredencialUsuarioSenha credCliente = new CredencialUsuarioSenha();
        credCliente.setInativo(false);
        credCliente.setNomeUsuario("cliente");
        credCliente.setSenha(codificador.encode("cliente123"));
        credCliente.setCriacao(new Date());
        credCliente.setUltimoAcesso(new Date());
        cliente.getCredenciais().add(credCliente);

        Endereco endCliente = new Endereco();
        endCliente.setEstado("São Paulo");
        endCliente.setCidade("São José dos Campos");
        endCliente.setBairro("Centro");
        endCliente.setRua("Av. Dr. Nelson D'Ávila");
        endCliente.setNumero("00");
        endCliente.setCodigoPostal("12245-070");
        cliente.setEndereco(endCliente);

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca("ABC-0000");
        veiculo.setModelo("Corolla Cross");
        veiculo.setTipo(TipoVeiculo.SUV);
        veiculo.setProprietario(cliente);
        cliente.getVeiculos().add(veiculo);
        empresa.getUsuarios().add(cliente);

        // ======= SERVIÇOS =======
        Servico trocaRodas = new Servico();
        trocaRodas.setDescricao("Troca das rodas do carro por novas");
        trocaRodas.setNome("Troca de Rodas");
        trocaRodas.setValor(50);
        empresa.getServicos().add(trocaRodas);

        Servico alinhamento = new Servico();
        alinhamento.setDescricao("Alinhamento das rodas do carro");
        alinhamento.setNome("Alinhamento de Rodas");
        alinhamento.setValor(50);
        empresa.getServicos().add(alinhamento);

        // ======= VENDA =======
        Venda venda = new Venda();
        venda.setCadastro(new Date());
        venda.setCliente(cliente);
        venda.getMercadorias().add(rodaLigaLeve);
        venda.setIdentificacao("1234698745");
        venda.setFuncionario(gerente);
        venda.getServicos().add(trocaRodas);
        venda.getServicos().add(alinhamento);
        venda.setVeiculo(veiculo);
        veiculo.getVendas().add(venda);
        empresa.getVendas().add(venda);

        repositorioEmpresa.save(empresa);
    }
}
