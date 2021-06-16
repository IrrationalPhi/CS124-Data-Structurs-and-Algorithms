library(ggplot2)

df <- read.csv("halfsim.txt")
strassen_df <- df[df["type"] == "strassen",]
traditional_df <- df[df["type"] == "traditional",]
strassen_df["type"] <- NULL
traditional_df["type"] <- NULL

ggplot(strassen_df, aes(x=size, y=time, colour = "label1")) + geom_line() +
  geom_line(data = traditional_df, aes(x=size, y=time, colour = "label2")) +
  ggtitle("Plot of average run time vs matrix size") +
  xlab("Matrix size") + ylab("Time (ms)") +
  scale_color_manual(labels = c("Strassen", "Traditional"),
                     values = c("dodgerblue", "firebrick"))
