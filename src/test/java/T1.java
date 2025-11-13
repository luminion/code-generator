import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.config.core.OutputConfig;
import io.github.luminion.generator.engine.VelocityTemplateEngine;

/**
 * @author luminion
 * @since 1.0.0
 */
public class T1 {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "root";
        Configurer configurer = new Configurer(url,username,password);
        OutputConfig outputConfig = configurer.getOutputConfig();
        outputConfig.setOutputFileGlobalOverride(true);
        outputConfig.setOutputDir("D:\\github\\test\\generator-test-sp3\\src\\main\\java");
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(configurer);
        configurer.getStrategyConfig().getInclude().add("sys_user");
        templateEngine.batchOutput().open();
        
        /*
        importPackages.add("com.baomidou.mybatisplus.core.metadata.IPage");
         */
    }
    
}
