import requests

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

#deleting only 1 show from multiple bookings by a user and checking if the balance is correct 
def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    showID = 7
    walletAmount = 1000
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

def delete_show(user_id, show_id):
    response = requests.delete(bookingServiceURL + f"/bookings/users/{user_id}/shows/{show_id}")
    return response

def delete_users():
    requests.delete(userServiceURL + f"/users")

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response

def add_money_and_check_detail(name, email, showID, walletAmount):
    try:
        delete_users()
        new_user = create_user(name, email)
        new_userid = new_user.json()['id']
        update_wallet(new_userid,"credit",walletAmount)
        new_booking = {"show_id": showID, "user_id": new_userid, "seats_booked": 1}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        new_booking = {"show_id": 1, "user_id": new_userid, "seats_booked": 1}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        delete_show(new_userid, showID) 
        balance = get_wallet(new_userid).json()['balance']
       
        if balance == walletAmount - get_show_details(1).json()['price'] :
            print("test case passed")
        else:
            print("test case failed")
    except Exception as e:
      
        print("Some Exception Occurred")


if __name__ == "__main__":
    main()