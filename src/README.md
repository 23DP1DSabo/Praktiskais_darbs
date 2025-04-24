# Praktiskais darbs "Bankas sistēma"
## Darba autori: Nils Pozņaks un Daniels Šabovičs
## Kurss: DP2-1
---
# Projekta iespēju apraksts
Sistēmas mērķis ir, iepazīstināt lietotāju ar *OOP (Objektorientētas Programmēšanas Pamatprincipiem)* un ar to kā darbojas bankas konti, kartes un saistība starp tiem, un tas tiek realizēts ar tādām funkcijām kā: 
- Kontu atvēršana un pārslēgšanās starp tiem;
- Debit un kredītkaršu izveide, bloķēšana, dzēšana;
- Naudas pārskaitīšana citiem kontiem;
- Nodokļu kalkulators;
- Veikto darbību vēstures apskatīšana;
- Datu filtrēšana un kārtošana;

**Programma ir domāta tikai izglītojošiem nolūkiem un nav paredzēta izplatīšanai, pārdošanai vai implementēšanai reālās finanšu sistēmās, kas darbojas ar reālu valūtu.**

# Lietotāja interfeisa apraksts
Šī ir konsoles programma, tāpēc noformējums ir veikts pārsvarā ar *ASCII un konsoles krāsu* palīdzību. ASCII izpaužas ar interfeisu un tabulām, ērtākai dat pārskatīšanai. Savukārt ar krāsām ir attēlotas, lietotāja veidotās bankas kartes.

# Lietotāja interfeisa struktūra
Darbības, pārsvarā, tiek veiktas ar opciju izvēlni, kur, atkarībā no jau veiktajām darbībām, ir piedāvatas dažādu funkciju veikšanai. 

### Izvēlnes piemērs:

| Option desc.          | Symbol |
| --------------------- | ------ |
| Create an account     |   C    |
| View accounts         |   V    |
| Sort accounts         |   S    |
| Transfer money        |   T    |
| Card managment        |   CC   |
| View transfer history |   H    |
| Tax calculator        |  Tax   |
| Log out               |  L-OUT |
| Exit the program      |   E    |
| --------------------- | ------ |
| Choose an option:     |        |

"Option desc." - Pieejamās opcijas
"Symbol" - rakstāmais simbols opcijas izvēlei

---
# Funkcionalitāte
Šeit tiks norādītas un sīkāk aprakstītas pieejamās funkcijas.

## Vieša izvēlne:

### Lietotāja reģistrācija
Ievadot simbolu "R", lietotājam izvada citu lapu, kur tam pieprasa papildus ievadīt savu jaunu lietotājvārdu. To ievadot, lietotājs tiek sekmīgi reģistrēts un tam tiek piešķirts lietotāja ID. ID, šajā gadījumā, aizvieto paroli.

### Pieslēgšanās eksistējošam lietotājam 
Ievadot simbolu "L", lietotājam izvada citu lapu, kur tam pieprasa ievadīt savu eksistējošu lietotājvārdu. Ievadot pareizi, lietotājs sekmīgi pieslēdzas. 

### Pārtraukt programmu
Ievadot simbolu "E", programma aizveras.

## Lietotāja izvēlne:

### Konta atvēršana
Ievadot simbolu "C", lietotājam izvada citu lapu

### Kontu pārskats
Ievadot simbolu "V", lietotājam izvada citu lapu

### Kontu kārtošana
Ievadot simbolu "S", lietotājam izvada citu lapu

### Naudas pārskaitīšana
Ievadot simbolu "T", lietotājam izvada citu lapu

### Debit un kredītkaršu izveide un pārlūkošana
Ievadot simbolu "CC", lietotājam izvada citu lapu

### Naudas pārskaitīšanas vēstures pārskatīšana
Ievadot simbolu "H", lietotājam izvada citu lapu

### Nodokļu kalkulators
Ievadot simbolu "Tax", lietotājam izvada citu lapu

## Karšu vadības izvēlne:

### Kredītkartes izveide
Ievadot simbolu "1", lietotājam izvada citu lapu

### Debit kartes izveide
Ievadot simbolu "2", lietotājam izvada citu lapu

### Aiziet atpakaļ uz lietotāja izvēlni
Ievadot simbolu "3", lietotājam izvada citu lapu

