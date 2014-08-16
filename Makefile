build:  
	@echo -e '\n[INFO] Compiling the Source..'
	mkdir out 
	javac -d out src/main/java/cs455/overlay/*/*/*.java 
	javac -cp out -d out src/main/java/cs455/overlay/*/*.java 
	javac -cp out -d out src/main/java/cs455/overlay/*.java 
	jar cvf overlay.jar -C out/ . 

registry:
	@echo -e '\n[INFO] Running the registry..'
	java -cp overlay.jar cs455.overlay.Registry 4000

node:
	@echo -e '\n[INFO] Running the node..'
	java -cp overlay.jar cs455.overlay.Messaging new-fork 4000

clean:
	@echo -e '\n[INFO] Cleaning Up..'
	rm -rf out/* 
	rm -rf overlay.jar  
	rm -rf out  


