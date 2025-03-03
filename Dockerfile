# Etapa 1: Construir a aplicação
FROM gradle:7.3.3-jdk17 AS build

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de build
COPY . .

# Executa o build
RUN gradle build --no-daemon

# Etapa 2: Imagem final
FROM openjdk:17-jdk-slim

# Variável de ambiente para permitir que o Spring Boot ou outro aplicativo busque arquivos de configuração
ENV SPRING_PROFILES_ACTIVE=prod

# Diretório onde a aplicação será executada
WORKDIR /app

# Copia o arquivo JAR gerado pela etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Expõe a porta que o aplicativo irá usar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
