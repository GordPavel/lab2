package GraficApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

class MyLoader extends ClassLoader{
//    Сохранение только что сканированных байт-кодов во временный кеш
    HashMap<String, Class> alreadyLoadedClasses;

    MyLoader( HashMap<String, Class> alreadyLoadedClasses ){
        this.alreadyLoadedClasses = alreadyLoadedClasses;
    }

    @Override
    protected Class<?> findClass( String name ) throws ClassNotFoundException{
        try{
            return loadClassFromFile( name );
        }catch( IOException e ){
            return null;
        }
    }

    private Class loadClassFromFile( String filename ) throws IOException{
        byte[] b = loadClassData( filename );
        return defineClass( null, b, 0, b.length );
    }

    private byte[] loadClassData( String filename ) throws IOException{
        FileInputStream in          = new FileInputStream( filename );
        byte[]          fileContent = new byte[ in.available() ];
        in.read( fileContent );
        in.close();
        return fileContent;
    }
}
