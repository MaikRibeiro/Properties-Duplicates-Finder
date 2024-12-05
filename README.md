# Duplicate Properties Finder

## Description
The Duplicate Properties Finder is a Java utility for validating `.properties` files. It identifies:
- Duplicate keys
- Duplicate values
- Keys without corresponding values

This tool is designed to ensure the correctness and consistency of property files.

---

## Features
- Detects duplicate keys and values.
- Lists keys without associated values.
- Displays results in an organized format.

---

## Requirements
- **Java 8 or later**
- Place the `.properties` file in the directory: `src/com/resources/`

---

### Expected Input .properties
key1 = value1
key2 = value2
key3 =
key1 = value3

---

### Sample Output
For the above file, the program outputs:
```
------------ Keys Without Values ------------
key3
------------ Duplicate Keys ------------
key1
------------ Duplicate Values ------------
value1
```

If there are no duplicates, the output will be:
`There are no duplicate keys or values.`

---

### Error Handling
If the file does not exist, the program will display:
`Error: src/com/resources/<file-name> (No such file or directory)`

If the file is empty or invalid:
`Enter the file name`

---

### **Português**

## Descrição
O Duplicate Properties Finder é uma ferramenta em Java para validar arquivos `.properties`. Ele identifica:
- Chaves duplicadas
- Valores duplicados
- Chaves sem valores correspondentes

Esta ferramenta garante a consistência e a validade dos arquivos de propriedades.

---

## Funcionalidades
- Detecta chaves e valores duplicados.
- Lista chaves sem valores associados.
- Exibe os resultados de forma organizada.

---

## Requisitos
- **Java 8 ou superior**
- Coloque o arquivo `.properties` no diretório: `src/com/resources/`

---

## Como Usar
1. Clone este repositório:
   ```bash
   git clone <url-do-repositorio>

## Formato Esperado
O arquivo .properties deve seguir o formato:
chave1 = valor 1
chave2 = valor 2
chave3 =
chave1 = valor 3

### Exemplo de Saída
Para o arquivo acima, o programa exibirá:
```
------------ Keys Without Values ------------
chave3
------------ Duplicate Keys ------------
chave1
------------ Duplicate Values ------------
valor1
```

Se não houver duplicados, a saída será:
`There are no duplicate keys or values.`

---

## Tratamento de Erros
Se o arquivo não existir, o programa exibirá:

`Error: src/com/resources/<nome-do-arquivo> (No such file or directory)`

Se o arquivo estiver vazio ou for inválido:
`Enter the file name`
