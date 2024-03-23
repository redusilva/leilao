import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CustomDateTime {

    // Método para obter a data e hora atual em UTC-03:00
    public Date getCurrentDate() {
        return new Date(
                OffsetDateTime
                        .now(ZoneId.of("UTC-03:00"))
                        .toInstant()
                        .toEpochMilli()
        );
    }

    // Método para converter uma string de data para UTC-03:00 e retornar como Date
    public Date parseDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        OffsetDateTime offsetDateTime = date.atStartOfDay(ZoneId.of("UTC-03:00")).toOffsetDateTime();
        return Date.from(offsetDateTime.toInstant());
    }

    public Date getTomorrowDate() {
        // Obter a data atual em UTC-03:00
        LocalDate currentDate = LocalDate.now(ZoneId.of("UTC-03:00"));

        // Adicionar um dia para obter a data de amanhã
        LocalDate tomorrowDate = currentDate.plusDays(1);

        // Converter para OffsetDateTime para manter o fuso horário e então para Date
        return Date.from(tomorrowDate.atStartOfDay(ZoneId.of("UTC-03:00")).toInstant());
    }

    public Date getYesterdayDate() {
        // Obter a data atual em UTC-03:00
        LocalDate currentDate = LocalDate.now(ZoneId.of("UTC-03:00"));

        // Subtrair um dia para obter a data de ontem
        LocalDate yesterdayDate = currentDate.minusDays(1);

        // Converter para OffsetDateTime para manter o fuso horário e então para Date
        return Date.from(yesterdayDate.atStartOfDay(ZoneId.of("UTC-03:00")).toInstant());
    }

}
