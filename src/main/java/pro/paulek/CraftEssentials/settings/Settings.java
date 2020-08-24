package pro.paulek.CraftEssentials.settings;

import com.google.common.collect.ImmutableMap;
import org.diorite.cfg.annotations.CfgClass;
import org.diorite.cfg.annotations.CfgComment;
import org.diorite.cfg.annotations.CfgName;
import org.diorite.cfg.annotations.CfgStringStyle;
import org.diorite.cfg.annotations.defaults.CfgDelegateDefault;

import java.util.Map;

@CfgClass(name = "Settings")
@CfgDelegateDefault("{new}")
@CfgComment("###########################################################")
@CfgComment(" §~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~§ #")
@CfgComment(" |                      Notatki                         | #")
@CfgComment(" §~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~§ #")
@CfgComment("###########################################################")
@CfgComment("Jezeli chcesz uzyc znakow specjalnych w tym configu, jak znaki specjalne - TEN PLIK MUSI ZOSTAC ZAPISANY W FORMACIE UTF-8")
public class Settings implements IConfiguration {

    @CfgComment("###########################################################")
    @CfgComment(" §~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~§ #")
    @CfgComment(" |                    Bazy Danych                       | #")
    @CfgComment(" §~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~§ #")
    @CfgComment("###########################################################")

    @CfgComment("Z jakiego rodzaju baz danych ma kozysztac plugin. Dostepne MySQL, SQLite")
    @CfgName("storage-type")
    @CfgStringStyle(CfgStringStyle.StringStyle.DEFAULT)
    public String storageType = "SQLite";

    @CfgComment("Dane logowania do bazdy danych MySQL")
    @CfgStringStyle(CfgStringStyle.StringStyle.ALWAYS_QUOTED)
    @CfgName("mysql")
    public Map<String, String> mysql = ImmutableMap.<String, String>builder()
            .put("jdbcUrl", "jdbc:mysql://{host}:{port}/{database-name}")
            .put("host", "localhost")
            .put("port", "3306")
            .put("user", "root")
            .put("password", "password")
            .put("database-name", "core")
            .put("pool-size", "10")
            .build();
    @CfgComment("Jaki prefix ma miec kazda tabela utworzona w bazie danych przez ten plugin")
    @CfgStringStyle(CfgStringStyle.StringStyle.ALWAYS_SINGLE_QUOTED)
    @CfgName("table-prefix")
    public String tablePrefix = "cloudylogin_";

    @CfgComment("Dane logowania do redisa")
    @CfgComment("Wersja uri bez hasla -> rediss://{host}:{port}")
    @CfgComment("Wersja uri z haslem -> redis://{password}@{host}:{port}")
    @CfgStringStyle(CfgStringStyle.StringStyle.ALWAYS_QUOTED)
    @CfgName("redis")
    public Map<String, String> redis = ImmutableMap.<String, String>builder()
            .put("uri", "redis://:{password}@{host}:{port}/{pool-size}")
            .put("host", "localhost")
            .put("port", "6379")
            .put("password", "")
            .put("pool-size", "10")
            .put("connectionTimeout", "10")
            .put("soTimeout", "10")
            .build();

    public void reload() {

    }

}
