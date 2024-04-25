chcp 65001

set JARS=lib\*
set MAIN_CLASS=edu.stanford.smi.protege.Application
set OPTIONS=-Xmx1000M
set ENCODING=-Dfile.encoding=UTF-8
set LIB_PATH=-Djava.library.path=lib

java %OPTIONS% %ENCODING% %LIB_PATH% -cp .;classes;%JARS%;src %MAIN_CLASS% 
