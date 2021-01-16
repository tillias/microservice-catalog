import requests
import json

base_url = "http://localhost:8080"


def authenticate():
    import_user = "import"
    import_password = "import"
    auth_url = base_url + "/api/authenticate"
    r = requests.post(auth_url, json={'username': import_user, 'password': import_password, 'rememberMe': 'false'})
    if r.status_code == requests.codes.ok:
        return r.json()['id_token']
    else:
        raise RuntimeError("Authentication failed")


def import_microservice(descriptor, bearer):
    import_url = base_url + "/api/import"
    r = requests.post(import_url, json=descriptor, headers={'Authorization': 'Bearer ' + bearer})
    if r.status_code == requests.codes.ok:
        print("Microservice has been imported")
    else:
        raise RuntimeError("Error importing microservice: " + r.text)


bearer = authenticate()

with open('.microservice') as json_file:
    data = json.load(json_file)
    import_microservice(data, bearer)
