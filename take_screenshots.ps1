Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

$srcPath = "e:\2ND SEMESTER\OOP\SecureUserCredentialManager"
$ssPath  = "$srcPath\screenshots"
New-Item -ItemType Directory -Force -Path $ssPath | Out-Null

function Take-FullScreenshot([string]$fileName) {
    $screen = [System.Windows.Forms.Screen]::PrimaryScreen.Bounds
    $bmp = New-Object System.Drawing.Bitmap($screen.Width, $screen.Height)
    $gfx = [System.Drawing.Graphics]::FromImage($bmp)
    $gfx.CopyFromScreen(0, 0, 0, 0, [System.Drawing.Size]::new($screen.Width, $screen.Height))
    $bmp.Save("$ssPath\$fileName", [System.Drawing.Imaging.ImageFormat]::Png)
    $gfx.Dispose(); $bmp.Dispose()
    Write-Host "Saved: $fileName"
}

# Launch the app
$proc = Start-Process "java" -ArgumentList "-cp `"$srcPath`" SecurePassManager" -PassThru
Start-Sleep -Seconds 3
Take-FullScreenshot "01_login_screen.png"

# Focus the window and Tab to Sign Up
Add-Type -TypeDefinition @"
using System;
using System.Runtime.InteropServices;
public class Win32 {
    [DllImport("user32.dll")] public static extern bool SetForegroundWindow(IntPtr h);
    [DllImport("user32.dll")] public static extern IntPtr FindWindowEx(IntPtr parent, IntPtr after, string cls, string title);
}
"@

$proc.Refresh()
$hwnd = $proc.MainWindowHandle
if ($hwnd -ne [IntPtr]::Zero) { [Win32]::SetForegroundWindow($hwnd) | Out-Null }

Start-Sleep -Milliseconds 500
# Tab through Username, Password fields to reach Sign Up button (4th tab)
[System.Windows.Forms.SendKeys]::SendWait("{TAB}{TAB}{TAB}{TAB}")
Start-Sleep -Milliseconds 300
[System.Windows.Forms.SendKeys]::SendWait(" ")
Start-Sleep -Seconds 1
Take-FullScreenshot "02_signup_screen.png"

$proc.Kill()
$proc.WaitForExit(3000)

Write-Host "`nDone! Check screenshots folder."
