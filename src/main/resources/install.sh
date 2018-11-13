#!/bin/bash
declare -a DISKS

#remove .s
rm -f changer/.s

echo "+-------------------------------------------------------------+"
echo "|     CHANGER     INSTALLER   |   PLEASE    STAY     CALM     |"
echo "+-------------------------------------------------------------+"
echo
echo "Hello, this is quick installer for simple Java file date Changer."
echo

# read names of disks
out="$(lsblk | grep '^[a-z]' | cut -f 1 -d " ")"
IFS=$'\n' read -rd '' -a disks <<< "$out"

echo "Available disks are:"
echo "$out"
echo -e "Please, specify disk name where Changer will be installed: \c"
read disk_name

# match input disk_name
flag=false
for f in "${disks[@]}"
do
    if [[ "$f" = "${disk_name}" ]]; then
        flag=true
    fi
done

#check
if [[ "${flag}" == false ]]; then
    echo
    echo "Sorry, but disk for specified name wasn't found. Abort."
    exit
fi

# calculate MD5 of id_serial_short of target disk
id="$(udevadm info --query=all --name=/dev/${disk_name} | grep ID_SERIAL_SHORT | cut -f 2 -d "=")"
con="$id some_salt"
md5="$(md5sum <<< $con | cut -f 1 -d " ")"

echo "$md5" > changer/.s

#specify path to install
echo
echo "Available mountpoints for ${disk_name} device:"
echo "$(lsblk | grep ${disk_name})"
echo -e "Please, specify full path where to locate Changer: \c"
read file_path


a="$(lsblk | grep '^[^A-Za-z0-9]' | grep $disk_name)"
IFS=$'\n' read -rd '' -a lines <<< "$a"

ismatch=false
for i in "${lines[@]}"
do
    IFS=$' ' read -rd '' -a arr <<< "$(echo $i | cut -d " " -f 1,7)"
    t=${arr[1]}
    if [[ "$file_path" == $(echo $t)* ]]; then
        ismatch=true
    fi
done

if [[ "${ismatch}" == true ]]; then
    echo "Choosen path: $file_path"
    echo "Installing..."
    cp -r changer/ "$file_path"
    echo "Installation has been successful!"
else
    echo "Choosen path \"$file_path\" is incorrect. Abort."
fi

