# Praktiskais darbs "Bankas sistēma"
## Darba autori: Nils Pozņaks un Daniels Šabovičs
## Kurss: DP2-1
---
# Projekta iespēju apraksts
Sistēmas mērķis ir, iepazīstināt lietotāju ar *OOP (Objektorientētas Programmēšanas Pamatprincipiem)* un ar to kā darbojas bankas konti, kartes un saistība starp tiem. Tas tiek realizēts ar tādām funkcijām kā: 
- Kontu atvēršana;
- Debit karšu izveide, apmaksa, dzēšana;
- Naudas pārskaitīšana citiem kontiem;
- Aizdevumu sistēma;
- Investīciju kalkulators;
- Veikto darbību vēstures apskatīšana;
- Datu filtrēšana un kārtošana;

**Programma ir domāta tikai izglītojošiem nolūkiem un nav paredzēta izplatīšanai, pārdošanai vai implementēšanai reālās finanšu sistēmās, kas darbojas ar reālu valūtu.**

# Lietotāja interfeisa apraksts un struktūra
Šī ir konsoles programma, tāpēc noformējums ir veikts pārsvarā ar *ASCII* palīdzību. ASCII izpaužas ar interfeisu un tabulām, ērtākai datu pārskatīšanai.

Darbības, pārsvarā, tiek veiktas ar opciju izvēlni, kur, atkarībā no jau veiktajām darbībām, ir piedāvatas dažādu funkciju veikšanai. 

### Izvēlnes piemērs:

```
| Option desc.          | Symbol |
+-----------------------+--------+
| Create an account     | C      |
| View accounts         | V      |
| Sort accounts         | S      |
| Transfer money        | T      |
| Card managment        | CC     |
| Deposit money         | D      |
| Widthdraw money       | W      |
| Invest calculator     | IC     |
| View transfer history | H      |
| Log out               | L-OUT  |
| Exit the program      | E      |
+-----------------------+--------+
Input your option:
```

"Option desc." - Pieejamās opcijas

"Symbol" - rakstāmais simbols opcijas izvēlei

---
# Programmas palaišana
Lai palaistu ielādēto sistēmu, ir nepieciešams:
- izvērst lejupielādēto .zip arhīvu
- atvērt komandu uzvedni (cmd)
- izmantojot komandu - `cd <directory>` - pāriet uz galveno projekta mapi
- palaist, izmantojot komandu: `java -cp bin Main`

Tāpat ir iespēja palaist programmu caur vscode vai citām IDE vidēm. 

---
# Funkcionalitāte
Šeit tiks norādītas un sīkāk aprakstītas visas pieejamās funkcijas.

## Vieša izvēlne:

### Lietotāja reģistrācija
Ievadot simbolu "R", lietotājam pieprasa papildus ievadīt savu jaunu lietotājvārdu. To ievadot, lietotājs tiek sekmīgi reģistrēts un tiek atvērta lietotāja izvēlne.

### Pieslēgšanās eksistējošam lietotājam 
Ievadot simbolu "L", viesim pieprasa ievadīt savu eksistējošu lietotājvārdu. Ievadot pareizi, lietotājs sekmīgi pieslēdzas.

### Pārtraukt programmu
Ievadot simbolu "E", programma tiet pārtraukta. 

## Lietotāja izvēlne:

### Konta atvēršana
Ievadot simbolu "C", lietotājam tiek pieprasīts izveidot konta lietotājvārdu un sākuma naudas summu. To ievadot, konts tiek atvērts un tam tiek piešķirts konta ID. ID, šajā gadījumā, aizvieto paroli.

### Kontu pārskats
Ievadot simbolu "V", lietotājam tiek parādīts saraksts ar izveidotajiem kontiem un to datiem.

### Kontu kārtošana
Ievadot simbolu "S", lietotājam tiek piedāvātas 2 opcijas par to kādā secībā sakārtot kontus. 

### Naudas pārskaitīšana
Ievadot simbolu "T", ja ir atvērti vismaz 2 konti, tiek pieprasīts konts no kura, un uz kuru veikt pārskaitījumu, un naudas summa ko pārskaitīt. Nevar norādīt to pašu kontu pie kura pašlaik ir pieslēgts lietotājs.

### Naudas ieskaitīšana
Ievadot simbolu "D", tiek simulēta, skaidras naudas ieskaitīšana kontā, glūži kā caur bankomātu.

### Nuadas izņemšana
Ievadow simbolu "W", no konta tiek noņemta nauda, attēlojot to kā lietotājs izņemj to ar bankomāta palīdzību. 

### Investīciju kalkulators
Ievadot simbolu "IC", lietotājam izvada investīciju kalkulatoru. Dati no tā netiek glabāti, jo tas ir vienkārši kalkulators. 

### Debit karšu izveide un pārlūkošana
Ievadot simbolu "CC", lietotājam izvada karšu izvēlni, kur ir iespēja pārvaldīt savas bankas kartes.

### Naudas pārskaitīšanas vēstures pārskatīšana
Ievadot simbolu "H", lietotājam tiek parādīta saraksts, kurā ir parādīti veitkie naudas pārskaitījumi uz pielsēgtā lietotāja.

## Karšu vadības izvēlne:

### Debit kartes izveide
Ievadot simbolu "1", lietotājam tiek izveidota debitkarte ar iepriekš norādītu PIN kodu. 

### Apmaksa ar karti
Ievadot simbolu "2" lietotājam tiek pieprasīta karte un summa, kuru apmaksāt, tā ietekmē naudas summu uz konta. 

### Karšu dzēšana
Ievadot simbolu "3", tiks dota izvēle par to, kādu karti dzēst.

### Aiziet atpakaļ uz lietotāja izvēlni
Ievadot simbolu "4", lietotāju pārved uz lietotāja izvēlni.

## Aizdevumu vadības izvēlne:

### Jauns aizdevums
Ievadot simbolu "1", no lietotāja tiek pieprasīts, konts, naudas summa un aizdevuma termiņš. Tos ievadot, norādītajā kontā tiek ieskaitīta nauda un tiek reģistrēts aizdevums, kuru ir jāatmaskā bankai. 

### Aizdevumu pārskats
Ievadot simbolu "2" tiek izvadīti visi veiktie aizdevumi un dažāda informācija par tiem. 

### Aizdevuma atmaksāšana
Ievadot simbolu "3" tiek pieprasīts eksistējoša aizdevuma ID un naudas summa, ko lietotājs vēlas atmaksāt bankai. Atmaksājot pilnu summu, aizdevums tiek reģistrēts kā "atmaksāts". 