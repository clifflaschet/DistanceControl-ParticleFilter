filterResults <- read.csv("filterResults_distance_sigma_8.txt", header = FALSE)
carPosition <- t(read.csv("carPosition.txt", header = FALSE))

setwd("./img/")
png(file="example%02d.png", width=500, height=500)
for (i in c(1:nrow(filterResults))){
  
  #Setup plot
  plot(t(filterResults[i,1]), xaxt = 'n', xlab="", ylim=c(-10,100), ylab = "Distance to vehicle ahead")
  
  #Plot vehicle ahead
  rect(0.96, -10, 1.04, 0, density = 50)
  
  #Plot vehicle
  rect(0.96, carPosition[i,1], 1.04, carPosition[i,1]+4)
  
  #Plot each particle
  for(j in c(1:ncol(filterResults))){
    points(1, filterResults[i,j])
  }
}
dev.off()

#Use ImageMagick to create a gif
system("convert -delay 10 *.png example_1.gif")

#Remove png files
file.remove(list.files(pattern=".png"))
