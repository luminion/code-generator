import io.github.luminion.generator.GeneratorHelper;

/**
 * @author luminion
 * @since 1.0.0
 */
public class T1 {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "root";
        String dir = "C:\\Project\\github\\test\\generator-test-sp3\\src\\main\\java";
        GeneratorHelper.mybatisPlusGenerator(url, username, password)
                .global(g->g.outputDir(dir))
                .execute("sys_user");
    }
    
}
