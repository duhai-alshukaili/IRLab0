package om.edu.utas.ibri;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.document.Field;

/**
 *
 * @author ispace
 */
public class TextFileIndexer {

    private static final String INDEX_DIR = "index";

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        File inputDirectory = new File("data");
        
        FileFilter txtFilter
                = (File file) -> file.getName().endsWith(".txt");

        indexDirectory(inputDirectory, txtFilter);
    }

    /**
     *
     * @param inputDirectory
     * @throws IOException
     */
    private static void indexDirectory(File dir,
            FileFilter filter) throws IOException {
        
        IndexWriter writer = createIndexWriter();
        
        System.out.println("Inside indexDirectory");

        try {
            for (File f : dir.listFiles(filter)) {
                
                System.out.println(f.getName());
                
                if (f.isDirectory()) {
                    
                    
                    indexDirectory(f, filter);
                } else {
                    indexFile(writer, f);
                }
            }
        } finally {
            writer.close();
        }
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    private static IndexWriter createIndexWriter() throws IOException {
        FSDirectory indexDir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexWriterConfig config
                = new IndexWriterConfig(new StandardAnalyzer());
        return new IndexWriter(indexDir, config);
    }

    /**
     *
     * @param writer
     * @param file
     * @throws IOException
     */
    private static void indexFile(IndexWriter writer,
            File file) throws IOException {
        Document doc = getDocument(file);
        
        System.out.printf("Indexing: %s/%s\n", 
                file.getParent(), file.getName());
        writer.addDocument(doc);
    }

    private static Document getDocument(File file) throws IOException {
        Document doc = new Document();

        doc.add(new TextField("contents", new FileReader(file)));
        doc.add(new StringField("filename", 
                file.getName(), Field.Store.YES));
        doc.add(new StringField("fullpath", 
                file.getCanonicalPath(), Field.Store.YES));

        return doc;
    }

}
