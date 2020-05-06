JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Building.java \
		HeapNode.java \
		risingCity.java \
		MinHeap.java \
		RedBlackNode.java \
		RedBlackTree.java		

default: classes
	
classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class