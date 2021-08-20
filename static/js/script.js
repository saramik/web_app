var korisnikUloga = "";
function getFormData($form) { //ucitava sa svake forme u bilo kom .html fajlu
	var unindexedArray = $form.serializeArray();
	var indexedArray = {};
	
	$.map(unindexedArray, function(n, i){
		indexedArray[n['name']] = n['value'];
    });

    return indexedArray;
}
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function login() {
	var data = getFormData($("#login")); 
	var s = JSON.stringify(data); 
	$.ajax({
		url: "login",
		type: "POST",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText); 
			korisnikUloga = d.uloga;
            if(korisnikUloga.localeCompare("class model.Administrator")==0)
                window.location.replace("/adminPocetna.html");
            else if(korisnikUloga.localeCompare("class model.Kupac")==0)
                window.location.replace("/kupacPocetna.html");
            else if(korisnikUloga.localeCompare("class model.Dostavljac")==0)
                window.location.replace("/dostavljacPocetna.html");
            else
                window.location.replace("/menadzerPocetna.html");
		}, error: function(e){
		    if(e.status == 400){
		        alert(e.responseText);
		        window.location.replace("/");
		    }
		}
	});
}

function isLoggedIn() {
	$.ajax({
		url: "isLoggedIn",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			korisnikUloga = d.uloga;
			if(d.loggedIn) {
				if(korisnikUloga.localeCompare("class model.Administrator")==0)
                    window.location.replace("/adminPocetna.html");
                else if(korisnikUloga.localeCompare("class model.Kupac")==0)
                    window.location.replace("/kupacPocetna.html");
                else if(korisnikUloga.localeCompare("class model.Dostavljac")==0)
                    window.location.replace("/dostavljacPocetna.html");
                else
                    window.location.replace("/menadzerPocetna.html");
			}
		}
	});
}

function isLoggedOut() {
	$.ajax({
		url: "isLoggedIn",
		type: "GET",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			if(!d.loggedIn) {
				window.location.replace("/");
			}
		}
	});
}

function logout(){
    $.ajax({
    		url: "logout",
    		type: "GET",
    		complete: function(data) {
    			d = JSON.parse(data.responseText);
    			if(d.loggedOut) {
    				window.location.replace("/");
    			}
    		}
    	});
}

function dodajKorisnika(userRole){
    var data = getFormData($("#dodajKorisnika"));
    var s = JSON.stringify(data);
    var myURL = "";
    if(userRole.localeCompare("admin")==0)
        myURL = "registracijaDM";
    else
        myURL = "registracijaKupca";
    console.log(myURL);
    $.ajax({
        url: myURL,
        type: "POST",
        data: s,
        contentType: "application/json",
        dataType: "json",
        complete : function (data) {
            if(data.status == 400)
                alert(data.statusText);
            else
                {
                    alert("Uspesno ste se registrovali.")
                    window.location.replace("/");
                }
        }
    });
}

function registracijaUloga(){
    $.ajax({
    		url: "isLoggedIn",
    		type: "GET",
    		complete: function(data) {
    			d = JSON.parse(data.responseText);
    			if(d.loggedIn) {
                    if(korisnikUloga.localeCompare("class model.Dostavljac")==0 || korisnikUloga.localeCompare("class model.Menadzer")==0)
                        window.location.replace("/kupacPocetna.html");
                    else $("#kupac").remove();
    			}
    			else {
    			    $("#uloga").remove();
    			    $("#admin").remove();
    			}
    		}
    	});
}

function getUlogovan(){ // u izmeni profila
	$.ajax({
		url: "mojiPodaci",
		type: "GET",
		contentType: "application/json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			document.getElementById("ime").value = d.ime;
			document.getElementById("prezime").value = d.prezime;
			document.getElementById("korisnickoIme").value = d.korisnickoIme;
			document.getElementById("lozinka").value = d.lozinka;
			document.getElementById("datumRodjenja").value = d.datumRodjenja;
			document.getElementById(d.pol).checked = true;
		}
	});
}

function KorisnikKojiSeMenja(kor){
	if(kor.localeCompare("none")===0){
		getUlogovan();
		$("#obrisi_btn").hide();
		return;
	}
	console.log("KORISNIK " + kor);
	$.ajax({
		url: "sviKorisnici",
		type: "GET",
		contentType: "application/json",
		complete: function(data) {
			d = JSON.parse(data.responseText);
			for(let o of d) {
				if(o.email.localeCompare(kor)===0){
				    console.log(o);
					document.getElementById("ime").value = o.ime;
					document.getElementById("prezime").value = o.prezime;
					document.getElementById("korisnickoIme").value = o.korisnickoIme;
					document.getElementById("lozinka").value = o.lozinka;
					document.getElementById("datumRodjenja").value = d.datumRodjenja;
					document.getElementById(d.pol).checked = true;
					break;
				}
			}
		}
	});
}


function ucitajKorisnike() {
	$.ajax({
		url: "sviKorisnici",
		type: "GET",
		contentType: "application/json",
		dataType: "json",
		complete: function(data) {
			korisnici = JSON.parse(data.responseText);
			console.log(korisnici);
            var table = $("#tabela_ostali_podaci");
            $("#tabela_header tr").remove();
            $("#tabela_ostali_podaci tbody tr").remove();
            $("#tabela_header").append(tableHeader("korisnici"));
            for(let k of korisnici) {
                table.append(makeTableRowIzbor("korisnici",k));
            }
		}
	});
}

function tableHeader(izbor){
	var row = "";
	if(izbor == "korisnici"){
		row =
			`
				<tr>
					<th onclick="sortTable(0)" >Korisnicko ime</th>
					<th onclick="sortTable(1)">Ime</th>
					<th onclick="sortTable(2)">Prezime</th>
					<th>Pol</th>
					<th>Datum rodjenja</th>
					<th>Uloga</th>
					<th onclick="sortTable(6)">Broj bodova</th>
					<th>Tip kupca</th>
				</tr>
			`;
	} else if(izbor == "restorani"){
        row =
            `
                <tr>
                    <th>Naziv</th>
                    <th>Ocena</th>
                    <th>Tip</th>
                </tr>
            `;
    }
	return row;
}

function makeTableRowIzbor(izbor,obj) {
	var row = "";
	if(izbor == "korisnici" && obj.tipKupca){
       row =
        `<tbody>
            <tr>
                <td class='align-middle'>${obj.korisnickoIme}</span></td>
                <td class='align-middle'>${obj.ime}</td>
                <td class='align-middle'>${obj.prezime}</td>
                <td class='align-middle'>${obj.pol}</td>
                <td class='align-middle'>${obj.datumRodjenja}</td>
                <td class='align-middle'>${obj.uloga}</td>
                <td class='align-middle'>${obj.sakupljeniBodovi}</td>
                <td class='align-middle'>${obj.tipKupca}</td>
           </tr>
           </tbody>`;
    } else if(izbor == "korisnici" && !obj.tipKupca){
          row =
           `<tbody>
               <tr>
                   <td class='align-middle'>${obj.korisnickoIme}</span></td>
                   <td class='align-middle'>${obj.ime}</td>
                   <td class='align-middle'>${obj.prezime}</td>
                   <td class='align-middle'>${obj.pol}</td>
                   <td class='align-middle'>${obj.datumRodjenja}</td>
                   <td class='align-middle'>${obj.uloga}</td>
                   <td class='align-middle'>${obj.sakupljeniBodovi}</td>
                   <td class='align-middle'> </td>
              </tr>
              </tbody>`;
       } else if(izbor=="restorani"){
            row =
                  `<tbody>
                      <tr>
                          <td class='align-middle'>${obj.naziv}</td>
                          <td class='align-middle'>${obj.ocena}</td>
                          <td class='align-middle'>${obj.tip}</td>
                     </tr>
                     </tbody>`;
       }
	return row;
}

function izmeniKorisnika(){
	var data2 = getFormData($("#izmenaPodataka"));
	var s = JSON.stringify(data2);
	$.ajax({
		url: "mojiPodaci",
		type: "PUT",
		data: s,
		contentType: "application/json",
		dataType: "json",
		complete : function (data) {
			d = JSON.parse(data.responseText);
			if(d){
			    alert("Korisnik uspesno promenjen!")
			    window.location.replace("/");
			}
		}, error: function(e){
            if(e.status == 400){
                alert(e.statusText);
                window.location.replace("/");
            }
        }
	});
}

function sortTable(n) {
  var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
  table = document.getElementById("tabela_ostali_podaci");
  switching = true;
  dir = "asc";
  while (switching) {
    switching = false;
    rows = table.rows;
    for (i = 1; i < (rows.length - 1); i++) {
      shouldSwitch = false;
      x = rows[i].getElementsByTagName("TD")[n];
      y = rows[i + 1].getElementsByTagName("TD")[n];
      if (dir == "asc") {
        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
          shouldSwitch= true;
          break;
        }
      } else if (dir == "desc") {
        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
          shouldSwitch = true;
          break;
        }
      }
    }
    if (shouldSwitch) {
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
      switchcount ++;
    } else {
      if (switchcount == 0 && dir == "asc") {
        dir = "desc";
        switching = true;
      }
    }
  }
}

function ucitajRestorane(){
    $.ajax({
        url: "restorani",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        complete: function(data) {
            restorani = JSON.parse(data.responseText);
            console.log(restorani);
            var table = $("#tabela_ostali_podaci");
            $("#tabela_header tr").remove();
            $("#tabela_ostali_podaci tbody tr").remove();
            $("#tabela_header").append(tableHeader("restorani"));
            for(let k of restorani) {
                table.append(makeTableRowIzbor("restorani",k));
            }
        }
    });
}