import io.github.luminion.generator.GeneratorHelper;

/**
 * @author luminion
 * @since 1.0.0
 */
public class T1 {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.name"));
        System.out.println(System.getProperty("user.dir"));
        String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "123456";
        String dir = "D:\\Project\\github\\test\\generator-test-sp3\\src\\main\\java";
        GeneratorHelper.mybatisPlusGenerator(url, username, password)
                .global(g->g.outputDir(dir).fileOverride(true))
                .execute("sys_user");
    }
    
}