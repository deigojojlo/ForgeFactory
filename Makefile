# Variables
SRC_DIR := forgefactory/src/main/java
BIN_DIR := bin
MAIN_CLASS := main.java.view.GUI
JAVA_VERSION := 11

# Default target: Compile and run the application
.PHONY: all
all: run

# Compile all Java files
.PHONY: compile
compile:
	mkdir -p $(BIN_DIR)
	javac --release $(JAVA_VERSION) -d $(BIN_DIR) -cp $(SRC_DIR) $(shell find $(SRC_DIR) -name "*.java")

# Run the application
.PHONY: run
run: compile
	java -cp $(BIN_DIR) $(MAIN_CLASS)

# Clean compiled files
.PHONY: clean
clean:
	rm -rf $(BIN_DIR)
