import requests
import sys

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    delete_users()
    delete_wallets()
    
    name = "John Doe"
    email = "johndoe@mail.com"

    print(f"=> create_user({name}, {email})")
    response_create_user = create_user(name, email)
    print(f"<= create_user() response: {response_create_user.text}")

    if not test_create_user(name, email, response_create_user):
        print("create_user() Failed\n")
        return False
    
    try:
        new_user = response_create_user.json()
    except:
        return False
    
    user_id = new_user['id']

    print(f"=> get_wallet({user_id})")
    create_wallet(user_id)
    response_get_wallet = get_wallet(user_id)
    print(f"<= get_wallet() response: {response_get_wallet.text}")
    
    if not test_get_wallet(user_id, response_get_wallet):
        print("get_wallet() Failed\n")
        return False

    try:
        wallet = response_get_wallet.json()
    except:
        return False
    
    old_balance = wallet['balance']
    action = "credit"
    amount = 100
    
    print(f"=> update_wallet({user_id}, {action}, {amount})")
    response_update_wallet = update_wallet(user_id, action, amount)
    print(f"<= update_wallet() response: {response_update_wallet.text}")

    if not test_update_wallet(user_id, action, amount, old_balance, response_update_wallet):
        print("update_wallet() Failed.")
        return False

    return True
    
def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def test_create_user(name, email, response):
    try:
        new_user = response.json()
    except:
        return False

    if ('id' not in new_user):
        print("create_user() Failed: 'id' field not present in response.")
        return False
    else:
        if (not isinstance(new_user['id'], int)):
            print("create_user() Failed: 'id' field not an integer value.")
            return False

    if ('name' not in new_user):
        print("create_user() Failed: 'name' field not present in response.")
        return False
    elif (new_user['name'] != name):
        print("create_user() Failed: 'name' field incorrect in response.")
        return False

    if ('email' not in new_user):
        print("create_user() Failed: 'email' field not present in response.")
        return False
    elif (new_user['email'] != email):
        print("create_user() Failed: 'email' field incorrect in response.")
        return False
    
    if (len(new_user) != 3):
        print("create_user() Failed: Incorrect response format.")
        return False

    if (response.status_code != 201):
        print(f"create_user() Failed: HTTP 201 expected, got {response.status_code}")
        return False

    return True

def delete_users():
    requests.delete(userServiceURL+f"/users")

def delete_wallets():
    requests.delete(walletServiceURL+f"/wallets")

def create_wallet(user_id):
    requests.put(walletServiceURL+f"/wallets/{user_id}", json={"action":"credit", "amount":0})

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response

def test_get_wallet(user_id, response):
    
    user_res = requests.get(userServiceURL + f"/users/{user_id}")

    if(user_res.status_code != 404 and response.status_code == 404):
        return True

    try:
        payload = response.json()
    except:
        return False
    
    if ('user_id' not in payload):
        print("get_wallet() Failed: 'user_id' field not present in response.")
        return False

    if (not isinstance(payload['user_id'], int)):
        print("update_wallet() Failed: 'user_id' field not an integer value.")
        return False

    if ('balance' not in payload):
        print("get_wallet() Failed: 'balance' field not present in response.")
        return False
    
    if (not isinstance(payload['balance'], int)):
        print("get_wallet() Failed: 'balance' field not an integer value.")
        return False

    if (len(payload) != 2):
        print("get_wallet() Failed: Incorrect response format.")
        return False

    if (response.status_code != 200):
        print(f"get_wallet() Failed: HTTP 200 expected, got {response.status_code}")
        return False

    return True
    
def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def test_update_wallet(user_id, action, amount, old_balance, response):
    if (action == 'debit' and old_balance < amount and response.status_code != 400):
        print(f"get_wallet() Failed: insufficient balance, expected HTTP 400, got {response.status_code}.")
        return False

    try:
        payload = response.json()
    except:
        return False

    if ('user_id' not in payload):
        print("update_wallet() Failed: 'user_id' field not present in response.")
        return False

    if (not isinstance(payload['user_id'], int)):
        print("update_wallet() Failed: 'user_id' field not an integer value.")
        return False
        
    if ('balance' not in payload):
        print("get_wallet() Failed: 'balance' field not present in response.")
        return False
    
    if (not isinstance(payload['balance'], int)):
        print("get_wallet() Failed: 'balance' field not an integer value.")
        return False

    if (len(payload) != 2):
        print("update_wallet() Failed: Incorrect response format.")
        return False
        
    if (action == 'credit' and payload['balance'] != old_balance + amount):
        print(f"get_wallet() Failed: 'balance' field incorrectly updated, expected {old_balance + amount}, got {payload['balance']}.")
        return False

    if (action == 'debit' and payload['balance'] != old_balance - amount):
        print(f"get_wallet() Failed: 'balance' field incorrectly updated, expected {old_balance - amount}, got {payload['balance']}.")
        return False
        
    if (response.status_code != 200):
        print(f"update_wallet() Failed: HTTP 200 expected, got {response.status_code}")
        return False

    return True

if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
