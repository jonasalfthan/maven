	SHELL = /bin/sh
	RELEASE_VERSION=#release version without user's prompt if non-empty property value; otherwise prompt
	MVN_LOG=$(ROOT)/mvn.log
	MVN_ARGS=-Dmaven.repo.local=build/maven/.m2	--errors -DreleaseVersion=$(RELEASE_VERSION) -s src/main/config/settings.xml #--log-file $(MVN_LOG)
	
	ROOT=$(shell pwd)
	MVN_DIR=apache-maven-3.0.4
	MVN_BIN_URL=http://tux.rainside.sk/apache/maven/binaries
	MVN_BIN_EXT=bin.tar.gz
	MVN_BIN=$(MVN_DIR)-$(MVN_BIN_EXT)
	MVN_URL=$(MVN_BIN_URL)/$(MVN_BIN)
	M2_HOME=$(ROOT)/build/maven/$(MVN_DIR)
	M2=$(M2_HOME)/bin
	#export PATH=$M2:$PATH
	export MAVEN_OPTS=-Xms16m -Xmx128m -Dfile.encoding=ISO-8859-1

#.PHONY: configure
	
# target: all - Default target.
#git checkout KentBeck src/main/java/junit/runner/Version.java
#git checkout KentBeck pom.xml
#git checkout KentBeck pom-dep.xml
all: configure clean release deploy

# target: help - Display callable targets.
help:
	egrep "^# target:" [Mm]akefile

# target: configure - Configure build process.
configure:
	if [ ! -d $(ROOT)/build/maven/$(MVN_DIR) ] ;then \
		@cd $(ROOT)/build/maven; wget $(MVN_URL); \
		cd $(ROOT)/build/maven; tar xvzf $(MVN_BIN); \
		cd $(ROOT)/build/maven; rm -rf $(MVN_BIN); \
		cd $(ROOT)/build/maven; chmod -R a+rX $(MVN_DIR); \
		cd $(ROOT); \
	fi; \
	cd $(ROOT); touch $(MVN_LOG); \
	#echo -n "Enter branch name : "; read BRANCH;
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) --version; \
	cd $(ROOT)
	
# target: clean - Cleanup target dir.
clean:
	cd $(ROOT); touch $(MVN_LOG); \
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -f pom.xml clean; \
	cd $(ROOT)

# target: compile - Compiles (if necessary) without installing artifacts.
compile:
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) --batch-mode -f pom.xml compile; \
	cd $(ROOT)

# target: test - Tests without compilation.
test:
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) --batch-mode -f pom.xml test; \
	cd $(ROOT)

# target: release - Prompts the user to modify *-SNAPSHOT version to a new release, and updates junit.runner.Version#id().
release:
	#git checkout KentBeck pom.xml
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -f pom.xml release:update-versions; \     # update junit:junit from SNAPSHOT version
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -f pom-dep.xml release:update-versions; \ # update junit:junit-dep from SNAPSHOT version
	cd $(ROOT)
	
# target: install - Installs (and compiles if necessary) the artifacts to local repository.
install:
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -DskipTests=true --batch-mode -f pom.xml install; \
	cd $(ROOT)
	
# target: deploy - Deploys artifacts junit:junit and junit:junit-dep to remote repository Maven central and Sourceforge ftp server.
deploy:
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -DskipTests=false -f pom-dep.xml install; \                  # junit:junit-dep not deploy to sourceforge
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -DskipTests=true  -f pom.xml     deploy; \                   # only junit:junit to be deploy to sourceforge
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -DskipTests=true  -f pom.xml     gpg:sign-and-deploy-file; \ # deploy junit:junit to Maven central
	cd $(ROOT); exec $(M2)/mvn $(MVN_ARGS) -DskipTests=true  -f pom-dep.xml gpg:sign-and-deploy-file; \ # deploy junit:junit-dep to Maven central
	cd $(ROOT)