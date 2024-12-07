import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

#chekcing if the application can handle negative amount in the credit

def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    showID = 7
    walletAmount = -1000
    add_money_and_check_detail(name, email, showID, walletAmount)


def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response


def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response


def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action": action, "amount": amount})
    return response

def delete_users():
    requests.delete(userServiceURL + f"/users")


def add_money_and_check_detail(name, email, showID, walletAmount):
    try:
        delete_users()
        new_user = create_user(name, email)
        new_userid = new_user.json()['id']
        
        if update_wallet(new_userid, "credit", walletAmount).status_code == 400:
            print("test case passed")
        else:
            print("test case failed")
    except Exception as e:
      
        print("Some Exception Occurred")


if __name__ == "__main__":
    main()