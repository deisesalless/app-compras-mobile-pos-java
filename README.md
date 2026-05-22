# Gerenciador de Lista de Compras

## Sobre o projeto

O **Gerenciador de Lista de Compras** é um aplicativo Android desenvolvido em **Java** com o objetivo de auxiliar usuários na organização de compras do dia a dia de forma simples e prática.

A proposta do projeto é permitir o gerenciamento de itens de compra, ajudando no controle de produtos que serão adquiridos em supermercados, feiras ou outros estabelecimentos.

O projeto foi desenvolvido durante a pós-graduação com foco em aprendizado, aplicando conceitos fundamentais do desenvolvimento Android nativo, como navegação entre telas, componentes visuais, internacionalização, formulários e manipulação de dados.

Atualmente, o aplicativo possui funcionalidades básicas de **CRUD (Create, Read, Update e Delete)** para gerenciamento de itens da lista de compras.

Além disso, o projeto também foi pensado para futuramente incluir funcionalidades como:

- Controle de gastos mensais
- Histórico de compras
- Controle de alimentos perecíveis
- Alertas de validade
- Previsão de gastos da compra

> **Observação:** essas funcionalidades ainda estão em fase de planejamento e desenvolvimento.

---

## Funcionalidades

### Cadastro e gerenciamento de itens

O aplicativo permite:

- Cadastrar itens na lista de compras
- Editar itens cadastrados
- Remover itens da lista
- Visualizar os itens adicionados

Cada item possui os seguintes campos:

- **Nome do item** (`String`)
- **Quantidade** (`int`)
- **Valor unitário** (`double`)
- **Categoria** selecionada em lista
- **Unidade de medida** utilizando `RadioGroup`
- **Indicação de item perecível** utilizando `CheckBox`

---

## Tecnologias

- **Java 21**
- **Gradle 9.1.0**
- **Android Studio 2025.3.2**

---

## Aprendizados

Desenvolvi este projeto durante a minha pós-graduação com o objetivo de aprender os conceitos básicos do desenvolvimento Android utilizando **Java**, além de entender como estruturar e construir um aplicativo mobile simples na prática.

Durante o desenvolvimento, aprendi:

- Como realizar a navegação entre telas e páginas do aplicativo
- A importância de adaptar o aplicativo ao tema padrão do dispositivo, permitindo suporte aos modos **Light** e **Dark**
- Boas práticas de usabilidade, como incluir uma página **“Sobre”** no aplicativo
- Como implementar suporte a múltiplos idiomas utilizando arquivos `strings.xml`
  - Exemplo: caso o idioma principal do aplicativo seja inglês, mas o dispositivo esteja configurado em **PT-BR** e exista o arquivo `/values-pt-rBR/strings.xml`, o aplicativo será exibido em português. Caso o idioma do dispositivo não esteja configurado no projeto, o aplicativo utilizará automaticamente o idioma padrão
- Como criar e configurar menus com textos e ícones
- Como gerar ícones personalizados a partir de imagens
  - Exemplo: ícones de editar, limpar, salvar e deletar
- Como exibir alertas e mensagens de feedback para o usuário
  - Exemplo: “Informações salvas com sucesso”, “Item editado com sucesso” ou “Telefone inválido”
