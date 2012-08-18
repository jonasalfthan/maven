	SHELL = /bin/sh
	ROOT=$(shell pwd)
	MVN_DIR=apache-maven-3.0.4
	MVN_BIN_URL=http://tux.rainside.sk/apache/maven/binaries
	MVN_BIN_EXT=bin.tar.gz
	MVN_BIN=$(MVN_DIR)-$(MVN_BIN_EXT)
	MVN_URL=$(MVN_BIN_URL)/$(MVN_BIN)
	MVN_LOG=$(ROOT)/mvn.log
	M2_HOME=$(ROOT)/build/maven/$(MVN_DIR)
	M2=$(M2_HOME)/bin
	#export PATH=$M2:$PATH
	export MAVEN_OPTS=-Xms16m -Xmx128m -Dfile.encoding=ISO-8859-1
	
all: configure release clean compile test install deploy
configure:
	echo ${user}; \
	if [ ! -d $(ROOT)/build/maven/$(MVN_DIR) ] ;then \
		@cd $(ROOT)/build/maven; wget $(MVN_URL); \
		cd $(ROOT)/build/maven; tar xvzf $(MVN_BIN); \
		cd $(ROOT)/build/maven; rm -rf $(MVN_BIN); \
		cd $(ROOT)/build/maven; chmod -R a+rX $(MVN_DIR); \
		cd $(ROOT); \
	fi; \
	cd $(ROOT); touch $(MVN_LOG); \
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 --batch-mode --version; \
	cd $(ROOT)
clean:
	cd $(ROOT); touch $(MVN_LOG); \
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 --batch-mode --errors clean; \ #--log-file $(MVN_LOG)
	cd $(ROOT)
compile:
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 -DskipTests=true --batch-mode --errors compile; \
	cd $(ROOT)
test:
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 --batch-mode --errors test; \
	cd $(ROOT)
release:
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 -DskipTests=true --errors release:update-versions; \
	cd $(ROOT)
install:
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 -DskipTests=true --batch-mode --errors javadoc:javadoc javadoc:jar install; \
	cd $(ROOT)
deploy:
	cd $(ROOT); exec $(M2)/mvn -Dmaven.repo.local=./build/maven/.m2 -DskipTests=true --errors -f pom.xml -s src/main/config/settings.xml gpg:sign-and-deploy-file; \
	cd $(ROOT)
