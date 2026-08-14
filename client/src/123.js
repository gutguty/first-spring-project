const a = () => {
    let count = 0

    return () => {
        count++;
        console.log(count);
    }
}

a();