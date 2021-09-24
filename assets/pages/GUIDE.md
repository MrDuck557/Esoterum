## Adding
Navigate to the [ManualPages.java](https://github.com/Goobrr/Esoterum/blob/master/src/esoterum/ui/ManualPages.java) file and append `makePage()` with your page file name (file extension included) as the function parameter to the relevant topic.

e.g `makePage("MyManualPage")`
## Formatting

### Text

Lines with no indicator at the start will be considered normal text until two newlines. Text styling is still the same as Mindustry's with hex color codes or color names in square brackets to set color (e.g ``[B00B69]`` or ``[scarlet]``)
```
This is line 1,
This is still line 1.

This is now line 2
[red]this is some red text in line2[] oh it's normal again.
```
 ### Headers
 
 Headers indicated by `[h]` at the start of the line, any following text will be considered to be header text until two newlines.
```
[h]This is header text,
this is still header text.

This is no longer header text.
```
### Images
 
 Images are indicated by `[i]` at the start of the line, the following text will be considered the name of the image sprite until it meets two newlines or `[t]` , where the following text will be considered to be caption text until it meets two newlines.
 ```
 [h]esoterum-sprite-name
 -still-a-sprite-name

[h]esoterum-another-sprite-name[t]Fig 1. this is caption text,
this is still caption text.

This is no longer caption text.
 ```
