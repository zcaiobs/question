FROM gradle:jdk21-alpine AS build

WORKDIR /app

# Copia apenas arquivos necessários para dependências, otimizando cache
#COPY build.gradle settings.gradle gradlew ./
#RUN chmod +x gradlew && ./gradlew dependencies

# Copia todo o projeto
COPY . .

# Executa o build da aplicação
RUN ./gradlew build --no-daemon

# Etapa 2: Imagem final
FROM gradle:jdk21-alpine AS final

# Diretório de trabalho para a aplicação final
WORKDIR /app

# Copiar os artefatos do build da etapa anterior
COPY --from=build /app/build/libs /app/libs

# Expor a porta onde o serviço será executado
EXPOSE 8080

# Definir o comando para rodar a aplicação Java
CMD ["sh", "-c", "java -jar $(ls /app/libs/*.jar | head -n 1)"]


