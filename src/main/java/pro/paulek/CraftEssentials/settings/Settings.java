package pro.paulek.CraftEssentials.settings;

import org.diorite.cfg.annotations.CfgClass;
import org.diorite.cfg.annotations.CfgComment;
import org.diorite.cfg.annotations.defaults.CfgDelegateDefault;

@CfgClass(name = "Settings")
@CfgDelegateDefault("{new}")
@CfgComment("###########################################################")
@CfgComment(" §~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~§ #")
@CfgComment(" |                      Notatki                         | #")
@CfgComment(" §~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~§ #")
@CfgComment("###########################################################")
@CfgComment("Jezeli chcesz uzyc znakow specjalnych w tym configu, jak znaki specjalne - TEN PLIK MUSI ZOSTAC ZAPISANY W FORMACIE UTF-8")
@CfgComment("Styl configu jest stylowany na configu Essentials https://github.com/EssentialsX - Poniewaz jest on latwy o obsludze")
@CfgComment("Aby plugin sie zaladowal, trzymaj sie tych zasad:")
@CfgComment("   - Nie uzywaj odstepow stworzonych tabem")
@CfgComment("   - Wciecia sa poprawne")
@CfgComment("   - Zamykaj wszystkie apostrofy")
@CfgComment("   - Kazda wartosc musi zostac zakonczona dwukropkiem")
@CfgComment("Konfiguracja dropow zamieszczona jest w osobnym pliku: drops.yml, natomiast podstawowa konfiguracja zamieszczona jest tutaj")
@CfgComment("Konfiguracja zestawow(kitow) zamieszczona jest w osobnym pliku: kits.yml")
public class Settings implements IConfiguration {

    public void reload() {

    }
}
