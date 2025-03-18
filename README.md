# Popis

1. Reprezentace tělesa pomocí vertex bufferu a index bufferu
	1. Těleso se může skládat z několika různých primitivních objektů, budeme tak muset mít víc typů index bufferu
2. Souřadnicové systémy
	1. Model space (Objektový prostor)
	2. World space
	3. View space (Prostor pozorovatele)
3. Modelová transformace - vezmu objekty a umístím je do prostoru
4. Pohledová transformace - Přesunu objekty relativně ku kameře, pomocí View Matrix
	1. Jakmile se přesuneme do View space, kamera je v počátku a kouká do -z, nahoru je y (up vektor)
5. Projekční transformace - Vybírám si mezi perspektivním pohledem a izometrickým pohledem
6. Ořezání - Jelikož můžou být různé části objektu v různých hloubkách a mohou se protínat, musíme je podle toho oříznout + ošetření, abych nevykresloval nic mimo raster. 
7. Dehomogenizace
8. Transformace do okna obrazovky - transformace z normalizovaných souřadnic do reálných souřadnic 
9. Z-Buffer

# TODO

- [x] Naprogramovat Z-Buffer
- [x] Otestovat Z-Buffer na jednom pixelu
- [x] Otestovat Z-Buffer na trojúhelníku
- [x] Rasterizace
- [ ] Těleso
- [x] Ořezání
