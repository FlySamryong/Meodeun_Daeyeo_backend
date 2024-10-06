FROM docker.elastic.co/elasticsearch/elasticsearch:7.17.9

# Nori 플러그인을 설치
RUN elasticsearch-plugin install analysis-nori