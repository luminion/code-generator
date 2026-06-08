package io.github.luminion.generator.enums;

import io.github.luminion.generator.config.Configurer;
import io.github.luminion.generator.util.InitializeUtils;

/**
 * 一键生成模式。
 *
 * @author luminion
 * @since 1.0.0
 */
public enum GenerationMode {
    MYBATIS {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeSuggestedExtraFieldSuffix(configurer);
            InitializeUtils.initializeMybatis(configurer);
        }
    },
    MYBATIS_PAGE_HELPER {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeSuggestedExtraFieldSuffix(configurer);
            InitializeUtils.initializeMybatisPageHelper(configurer);
        }
    },
    MYBATIS_PLUS {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeSuggestedExtraFieldSuffix(configurer);
            InitializeUtils.initializeMybatisPlus(configurer);
        }
    },
    MYBATIS_SQL_BOOSTER {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeSupportedExtraFieldSuffix(configurer);
            InitializeUtils.initializeMybatisSqlBooster(configurer);
        }
    },
    MYBATIS_PLUS_SQL_BOOSTER {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeSupportedExtraFieldSuffix(configurer);
            InitializeUtils.initializeMpBooster(configurer);
        }
    },
    MYBATIS_SQL_BOOSTER_CONTEXT {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeMybatisSqlBoosterContext(configurer);
        }
    },
    MYBATIS_PAGE_HELPER_SQL_BOOSTER_CONTEXT {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeMybatisPageHelperSqlBoosterContext(configurer);
        }
    },
    MYBATIS_PLUS_SQL_BOOSTER_CONTEXT {
        @Override
        public void initialize(Configurer configurer) {
            InitializeUtils.initializeMybatisPlusSqlBoosterContext(configurer);
        }
    };

    public abstract void initialize(Configurer configurer);
}
