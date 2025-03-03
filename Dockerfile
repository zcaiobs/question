# Etapa 1: Construção da aplicação
FROM gradle:7.3.3-jdk17 AS build

# Instalar dependências adicionais necessárias para o sistema de arquivos
RUN apt-get update && apt-get install -y \
    procps \
    lsof \
    && rm -rf /var/lib/apt/lists/*

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiar os arquivos do projeto para o diretório de trabalho
COPY . .

# Garantir que o diretório tenha as permissões corretas
RUN chmod -R 777 /app

# Executar o build com o Gradle Wrapper
RUN ./gradlew build --no-daemon

# Etapa 2: Imagem final
FROM openjdk:17-jdk-slim AS final

# Diretório de trabalho para a aplicação final
WORKDIR /app

# Copiar os artefatos do build da etapa anterior
COPY --from=build /app/build/libs /app/libs

# Expor a porta onde o serviço será executado
EXPOSE 8080

# Definir o comando para rodar a aplicação Java
CMD ["java", "-jar", "/app/libs/seu-jar-application.jar"]

