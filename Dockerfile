# Dockerfile
ARG VERSION=7.17.9
FROM docker.elastic.co/elasticsearch/elasticsearch:${VERSION}
RUN elasticsearch-plugin install analysis-nori
